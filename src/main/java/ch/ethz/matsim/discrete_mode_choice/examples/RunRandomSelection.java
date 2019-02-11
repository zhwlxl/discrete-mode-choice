package ch.ethz.matsim.discrete_mode_choice.examples;

import java.net.URL;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;

import ch.ethz.matsim.discrete_mode_choice.modules.DiscreteModeChoiceConfigurator;
import ch.ethz.matsim.discrete_mode_choice.modules.DiscreteModeChoiceModule;

public class RunRandomSelection {
	static public void main(String[] args) {
		URL configURL = IOUtils.newUrl(ExamplesUtils.getTestScenarioURL("siouxfalls-2014"), "config_default.xml");
		// URL configURL = IOUtils.newUrl(ExamplesUtils.getTestScenarioURL("equil"),
		// "config.xml");

		Config config = ConfigUtils.loadConfig(configURL);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setOutputDirectory("output");
		config.controler().setLastIteration(1);

		Scenario scenario = ScenarioUtils.loadScenario(config);

		Controler controller = new Controler(scenario);

		DiscreteModeChoiceConfigurator.configureAsSubtourModeChoiceReplacement(config);
		controller.addOverridingModule(new DiscreteModeChoiceModule());

		controller.run();
	}
}
