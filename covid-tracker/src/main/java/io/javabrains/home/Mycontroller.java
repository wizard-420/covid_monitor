package io.javabrains.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.javabrains.CoronaVirusDataService;
import io.javabrains.models.LocationStats;

@Controller
public class Mycontroller {
	
	@Autowired
	CoronaVirusDataService coronaVirusDataService;
	
	@GetMapping("/cases")
	public String home(Model model){
		List<LocationStats> allstats = coronaVirusDataService.getAllStats();
		int totalReportedCases = allstats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int toalNewCases = allstats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		model.addAttribute("locstat", allstats );
		model.addAttribute("totalReportedCases", totalReportedCases);
		model.addAttribute("toalNewCases", toalNewCases );
		return "home";
	}

}