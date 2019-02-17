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

import ch.ethz.matsim.baseline_scenario.config.CommandLine;
import ch.ethz.matsim.baseline_scenario.config.CommandLine.ConfigurationException;
import ch.ethz.matsim.discrete_mode_choice.examples.my_estimator.MyExtension;
import ch.ethz.matsim.discrete_mode_choice.modules.DiscreteModeChoiceConfigurator;
import ch.ethz.matsim.discrete_mode_choice.modules.DiscreteModeChoiceModule;
import ch.ethz.matsim.discrete_mode_choice.modules.EstimatorModule;
import ch.ethz.matsim.discrete_mode_choice.modules.SelectorModule;
import ch.ethz.matsim.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;

public class RunModeChoiceInTheLoop {
	static public void main(String[] args) throws ConfigurationException {
		CommandLine cmd = new CommandLine.Builder(args).build();
		URL configURL = IOUtils.newUrl(ExamplesUtils.getTestScenarioURL("siouxfalls-2014"), "config_default.xml");

		Config config = ConfigUtils.loadConfig(configURL);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setOutputDirectory("output_in_the_loop");
		config.controler().setLastIteration(1000);
		config.controler().setWriteEventsInterval(100);
		config.controler().setWritePlansInterval(100);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);

		Controler controller = new Controler(scenario);

		DiscreteModeChoiceConfigurator.configureAsSubtourModeChoiceReplacement(config);
		DiscreteModeChoiceConfigurator.configureAsModeChoiceInTheLoop(config);
		DiscreteModeChoiceConfigGroup dmcConfig = (DiscreteModeChoiceConfigGroup) config.getModules()
				.get(DiscreteModeChoiceConfigGroup.GROUP_NAME);
		cmd.applyConfiguration(config);

		dmcConfig.setTripEstimator("MyEstimatorName");
		dmcConfig.setTourEstimator(EstimatorModule.CUMULATIVE);
		dmcConfig.setSelector(SelectorModule.MULTINOMIAL_LOGIT);

		controller.addOverridingModule(new DiscreteModeChoiceModule());
		controller.addOverridingModule(new MyExtension());

		controller.run();
	}
}
