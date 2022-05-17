package io.github.junhaoshih.linebotserver.repository.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.junhaoshih.linebotserver.data.roc.RocCounty;
import io.github.junhaoshih.linebotserver.data.roc.RocRegion;
import io.github.junhaoshih.linebotserver.enums.cwb.County;
import io.github.junhaoshih.linebotserver.repository.RocCountyRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Repository
public class JsonRocCountyRepository implements RocCountyRepository {
    @Override
    public List<RocCounty> getRocCounties() throws IOException, InterruptedException {
        InputStream jsonFile = new ClassPathResource("CityCountyData.json").getInputStream();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(jsonFile, new TypeReference<List<RocCounty>>() {});
        /*HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(3000))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://raw.githubusercontent.com/JunHaoShih/TaiwanAddressCityAreaRoadChineseEnglishJSON/master/CityCountyData.json"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), new TypeReference<List<RocCounty>>() {});*/
    }

    @Override
    public boolean isRegionExist(County county, String regionStr) throws IOException, InterruptedException {
        List<RocCounty> rocCounties = getRocCounties();
        RocCounty targetCounty = rocCounties.stream().filter(rocCounty -> rocCounty.getCityName().equals(county.getCountyName())).findFirst().get();
        RocRegion targetRegion = targetCounty.getAreaList().stream().filter(rocRegion -> rocRegion.getAreaName().equals(regionStr)).findFirst().orElse(null);
        if (targetRegion != null)
            return true;
        else
            return false;
    }

    @Override
    public List<RocRegion> getRocRegions(County county) throws IOException, InterruptedException {
        List<RocCounty> rocCounties = getRocCounties();
        RocCounty targetCounty = rocCounties.stream().filter(rocCounty -> rocCounty.getCityName().equals(county.getCountyName())).findFirst().get();
        return targetCounty.getAreaList();
    }
}
