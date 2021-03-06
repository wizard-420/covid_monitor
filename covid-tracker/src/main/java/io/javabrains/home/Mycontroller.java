package io.javabrains.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.javabrains.CoronaVirusDataService;
import io.javabrains.models.DeathStats;
import io.javabrains.models.LocationStats;
import io.javabrains.models.RecoveryStats;

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
	@GetMapping("/deaths")
	public String death(Model model) {
		List<DeathStats> allstats = coronaVirusDataService.getDeathstat();
		int totalReportedCases = allstats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int toalNewCases = allstats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		model.addAttribute("dstat", allstats );
		model.addAttribute("dtotalReportedCases", totalReportedCases);
		model.addAttribute("dtoalNewCases", toalNewCases );
		return "death";
		
	}
	@GetMapping("/recovery")
	public String recovery(Model model) {
		List<RecoveryStats> allstats = coronaVirusDataService.getRecstats();
		int totalReportedCases = allstats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int toalNewCases = allstats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		model.addAttribute("rstat", allstats );
		model.addAttribute("rtotalReportedCases", totalReportedCases);
		model.addAttribute("rtoalNewCases", toalNewCases );
		return "recovery";
		
	}

}
