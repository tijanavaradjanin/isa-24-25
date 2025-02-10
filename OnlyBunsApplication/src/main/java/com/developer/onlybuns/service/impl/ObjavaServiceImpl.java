package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.dto.ObjavaDTO;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.service.ObjavaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Haversine formula za izračunavanje udaljenosti između dve tačke
    private static final double EARTH_RADIUS = 6371; // Kilometri

    public ObjavaServiceImpl(ObjavaRepository objavaRepository) {
        this.objavaRepository = objavaRepository;
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

    public double[] validateLocation(String grad, String drzava) {
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

    public List<ObjavaDTO> findNearbyPosts(RegistrovaniKorisnik korisnik) {
        double[] koordinate = validateLocation(korisnik.getGrad(), korisnik.getDrzava());

        if (koordinate == null) {
            return new ArrayList<>(); // Ako koordinate nisu pronađene, vraćamo praznu listu
        }

        double latKorisnik = koordinate[0];
        double lonKorisnik = koordinate[1];

        double radijus = 200.0; // Postavljamo radijus na 50 km, može se promeniti kasnije

        // Dobijamo sve objave iz baze
        List<Objava> allPosts = objavaRepository.findAll();
        List<ObjavaDTO> nearbyPosts = new ArrayList<>();

        for (Objava objava : allPosts) {
            // Provera da li objava pripada ulogovanom korisniku
            if (objava.getRegistrovaniKorisnik().getId().equals(korisnik.getId())) {
                continue; // Preskoči svoje objave
            }
            double latObjava = objava.getLatituda(); // Latitude objave
            double lonObjava = objava.getLongituda(); // Longitude objave

            double distance = calculateDistance(latKorisnik, lonKorisnik, latObjava, lonObjava);

            if (distance <= radijus) {
                nearbyPosts.add(new ObjavaDTO(
                        objava.getId(),
                        objava.getOpis(),
                        objava.getSlika(),
                        objava.getLatituda(),
                        objava.getLongituda(),
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
