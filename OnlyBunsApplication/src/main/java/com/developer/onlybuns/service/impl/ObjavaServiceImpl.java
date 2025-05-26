package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.dto.ObjavaDTO;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.service.LokacijaService;
import com.developer.onlybuns.service.ObjavaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ObjavaServiceImpl implements ObjavaService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    @Autowired
    private final ObjavaRepository objavaRepository;

    @Autowired
    private final LokacijaService lokacijaService;

    // Haversine formula za izračunavanje udaljenosti između dve tačke
    private static final double EARTH_RADIUS = 6371; // Kilometri

    public ObjavaServiceImpl(ObjavaRepository objavaRepository, LokacijaService lokacijaService) {
        this.objavaRepository = objavaRepository;
        this.lokacijaService = lokacijaService;
    }

    @Override
    public List<Objava> getAllObjave() {
        return objavaRepository.findAllObjave();
    }

    @Override
    public Optional<Objava> getById(Integer id) {
        return objavaRepository.findById(id);
    }

    @Override
    public void saveObjava(Objava objava) {
        objavaRepository.save(objava);
    }

    @Override
    public Objava updateObjava(Objava objava) {
        return objavaRepository.save(objava);
    }

    @Override
    public void deleteObjava(Integer id) {
        objavaRepository.deleteById(id);
    }

    @Override
    public List<Lajk> getAllLajkovi(Integer id) {
        Optional<Objava> objava = getById(id);
        List<Lajk> lajkovi = new ArrayList<>();
        if (objava != null) {
            lajkovi = objava.get().getLajkovi();
            return lajkovi;
        } else {
            return lajkovi;
        }
    }

    @Override
    public List<Komentar> getAllKomentari(Integer id) {
        Optional<Objava> objava = getById(id);
        List<Komentar> komentari = new ArrayList<>();
        if (objava != null) {
            komentari = objava.get().getKomentari();
            return komentari;
        } else {
            return komentari;
        }
    }

    @Override
    public List<Objava> findByKorisnikId(Integer korisnikId) {
        return objavaRepository.findByKorisnikId(korisnikId);
    }

    public double[] validateKorisnikLocation(String grad, String drzava) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = NOMINATIM_URL + grad + "," + drzava;
            String response = restTemplate.getForObject(url, String.class);

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() == 0) {
                return null; // Lokacija nije pronađena
            }

            JSONObject location = jsonArray.getJSONObject(0);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");

            return new double[]{lat, lon};
        } catch (Exception e) {
            return null; // Greška pri dohvaćanju podataka
        }
    }

    public double[] validateLocation(String grad, String drzava, String ulica, String broj) {
        try {
            StringBuilder sb = new StringBuilder();
            if (ulica != null && !ulica.isBlank()) sb.append(ulica);
            if (broj != null) sb.append(" ").append(broj);
            if (grad != null && !grad.isBlank()) sb.append(", ").append(grad);
            if (drzava != null && !drzava.isBlank()) sb.append(", ").append(drzava);

            String adresa = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?q=" + adresa + "&format=json";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("User-Agent", "OnlyBunsApp")
                    .header("Referer", "https://onlybuns.com")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Raw response: " + response.body());

            JSONArray jsonArray = new JSONArray(response.body());
            if (jsonArray.isEmpty()) {
                System.out.println("Nema rezultata za adresu: " + sb.toString());
                return null;
            }

            JSONObject location = jsonArray.getJSONObject(0);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");

            System.out.println("Latituda: " + lat);
            System.out.println("Longituda: " + lon);

            return new double[]{lat, lon};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ObjavaDTO> findNearbyPosts(RegistrovaniKorisnik korisnik) {

        double latKorisnik = korisnik.getLokacija().getLatituda();
        double lonKorisnik = korisnik.getLokacija().getLongituda();

        double radijus = 100.0; // Postavljamo radijus na 50 km, može se promeniti kasnije

        // Dobijamo sve objave iz baze
        List<Objava> allPosts = objavaRepository.findAll();
        List<ObjavaDTO> nearbyPosts = new ArrayList<>();

        for (Objava objava : allPosts) {
            // Provera da li objava pripada ulogovanom korisniku
            if (objava.getRegistrovaniKorisnik().getId().equals(korisnik.getId())) {
                continue; // Preskoči svoje objave
            }
            double latObjava = objava.getLokacija().getLatituda(); // Latitude objave
            double lonObjava = objava.getLokacija().getLongituda(); // Longitude objave

            double distance = calculateDistance(latKorisnik, lonKorisnik, latObjava, lonObjava);

            if (distance <= radijus) {
                String adresa=lokacijaService.getAdresa(latObjava, lonObjava);
                nearbyPosts.add(new ObjavaDTO(
                        objava.getId(),
                        objava.getOpis(),
                        objava.getSlika(),
                        objava.getLokacija().getLatituda(),
                        objava.getLokacija().getLongituda(),
                        adresa,
                        objava.getVremeKreiranja(),
                        objava.getKomentari(),
                        objava.getLajkovi(),
                        objava.getRegistrovaniKorisnik().getKorisnickoIme(),
                        objava.getLajkovi().size(),
                        objava.getKomentari().size()
                ));
            }
        }

        return nearbyPosts;
    }

    // Formula za računanje udaljenosti između dva koordinata (lat, lon)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Vraća udaljenost u kilometrima
    }

}