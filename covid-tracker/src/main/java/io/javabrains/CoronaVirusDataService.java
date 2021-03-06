package io.javabrains;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.javabrains.models.DeathStats;
import io.javabrains.models.LocationStats;
import io.javabrains.models.RecoveryStats;


@Service
public class CoronaVirusDataService {
	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	private static String DEATH_STAT = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
	private static String RECOVERED_STAT = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";
	private List<RecoveryStats> recstats = new ArrayList<>();
	private List<LocationStats> allStats = new ArrayList<>();
	private List<DeathStats> deathstat = new ArrayList<>();
	
	public List<DeathStats> getDeathstat() {
		return deathstat;
	}


	public List<LocationStats> getAllStats() {
		return allStats;
	}
	


	public List<RecoveryStats> getRecstats() {
		return recstats;
	}


	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException {
		List<LocationStats> newstats = new ArrayList<>();
		List<DeathStats> dstats = new ArrayList<>();
		List<RecoveryStats> rstats = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request  = HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStats locationStats = new LocationStats();
		    locationStats.setState(record.get("Province/State"));
		    locationStats.setCountry(record.get("Country/Region"));
		    int latestCases = Integer.parseInt(record.get(record.size() - 1));
		    int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
		    locationStats.setLatestTotalCases(latestCases);
		    locationStats.setDiffFromPrevDay(latestCases - prevDayCases);
		    newstats.add(locationStats);
		}
	this.allStats = newstats;	
	HttpClient dclient = HttpClient.newHttpClient();
	HttpRequest drequest  = HttpRequest.newBuilder()
			.uri(URI.create(DEATH_STAT))
			.build();
	HttpResponse<String> dhttpResponse = dclient.send(drequest, HttpResponse.BodyHandlers.ofString());
	StringReader dcsvBodyReader = new StringReader(dhttpResponse.body());
	Iterable<CSVRecord> drecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(dcsvBodyReader);
	for (CSVRecord record : drecords) {
		DeathStats deathStats = new DeathStats();
	    deathStats.setState(record.get("Province/State"));
	    deathStats.setCountry(record.get("Country/Region"));
	    int latestCases = Integer.parseInt(record.get(record.size() - 1));
	    int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
	    deathStats.setLatestTotalCases(latestCases);
	    deathStats.setDiffFromPrevDay(latestCases - prevDayCases);
	    dstats.add(deathStats);
	}
	this.deathstat = dstats;
	HttpClient rclient = HttpClient.newHttpClient();
	HttpRequest rrequest  = HttpRequest.newBuilder()
			.uri(URI.create(RECOVERED_STAT))
			.build();
	HttpResponse<String> rhttpResponse = dclient.send(rrequest, HttpResponse.BodyHandlers.ofString());
	StringReader rcsvBodyReader = new StringReader(rhttpResponse.body());
	Iterable<CSVRecord> rrecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(rcsvBodyReader);
	for (CSVRecord record : rrecords) {
		RecoveryStats recstats = new RecoveryStats();
	    recstats.setState(record.get("Province/State"));
	    recstats.setCountry(record.get("Country/Region"));
	    int latestCases = Integer.parseInt(record.get(record.size() - 1));
	    int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
	    recstats.setLatestTotalCases(latestCases);
	    recstats.setDiffFromPrevDay(latestCases - prevDayCases);
	    rstats.add(recstats);
	}
	this.recstats = rstats;
 }

}
