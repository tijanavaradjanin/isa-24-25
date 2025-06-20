package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.repository.LokacijaRepository;
import com.developer.onlybuns.service.LokacijaService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LokacijaServiceImpl implements LokacijaService {

    @Autowired
    public LokacijaRepository lokacijaRepository;

    public LokacijaServiceImpl(LokacijaRepository lokacijaRepository) {
        this.lokacijaRepository = lokacijaRepository;
    }

    private final Logger LOG = LoggerFactory.getLogger(LokacijaServiceImpl.class);

    @Override
    public void saveLokacija(Lokacija lokacija){
        lokacijaRepository.save(lokacija);
    }

    @Cacheable(value = "lokacijaCache", key = "#id", unless = "#result == null")
    public Lokacija findById(Integer id) {
        LOG.info("Fetching from DB");
        return lokacijaRepository.findById(id).orElse(null);
    }

    //kesiranje adrese koja se koristi pri prikazu objava i profila korisnika
    @Cacheable(value = "lokacijaAdresaCache", key = "#latituda + '_' + #longituda", unless = "#result == null")
    public String getAdresa(double latituda, double longituda) {
        LOG.info(">>> FETCHING address from API for: {} , {}", latituda, longituda);
        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json",
                    latituda, longituda
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "OnlyBunsApp");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JSONObject jsonObject = new JSONObject(response.getBody());
            if (jsonObject.has("display_name")) {
                return jsonObject.getString("display_name");
            } else {
                return "Nepoznata lokacija";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Greška pri dohvatanju adrese";
        }
    }

    @CacheEvict(cacheNames = {"lokacijaCache", "lokacijaAdresaCache"}, allEntries = true)
    public void removeFromCache() {
        LOG.info("Locations removed from cache!");
    }

}
