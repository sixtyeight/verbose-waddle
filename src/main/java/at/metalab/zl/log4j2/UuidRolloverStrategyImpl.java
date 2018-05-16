package at.metalab.zl.log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescriptionImpl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "UuidRolloverStrategy", category = Core.CATEGORY_NAME, printObject = true)
public class UuidRolloverStrategyImpl extends DirectWriteRolloverStrategy {

	private String directory;

	private String prefix;

	public UuidRolloverStrategyImpl(String directory, String prefix) {
		super(Integer.MAX_VALUE, 0, null, null, false, null);
		this.directory = directory;
		this.prefix = prefix != null ? prefix : "zl";
	}

	public String getCurrentFileName(RollingFileManager manager) {
		try {
			Files.createDirectories(new File(directory).toPath());
		} catch (IOException ioException) {
			throw new RuntimeException("Could not create missing directories.", ioException);
		}

		String newFilename = String.format("%s/%s_%s_%s.log", directory, prefix, getTimestamp(),
				UUID.randomUUID().toString());
		return newFilename;
	}

	private static String getTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss.SSS").format(new Date());
	}

	@Override
	public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
		return new RolloverDescriptionImpl(getCurrentFileName(manager), false, null, null);
	}

	@PluginFactory
	public static UuidRolloverStrategyImpl createStrategy(
			// @formatter:off
			@PluginAttribute("directory") final String directory, @PluginAttribute("prefix") final String prefix,
			@PluginConfiguration final Configuration config) {
		return new UuidRolloverStrategyImpl(directory, prefix);
	}
}
