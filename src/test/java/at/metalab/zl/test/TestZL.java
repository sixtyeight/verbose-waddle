package at.metalab.zl.test;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestZL {

	public static final Logger LOG = LogManager.getLogger(TestZL.class);

	public static void deep1() throws Exception {
		deep2();
	}

	public static void deep2() throws Exception {
		deep3();
	}

	public static void deep3() throws Exception {
		deep4();
	}

	public static void deep4() throws Exception {
		throw new IOException("something went wrong");
	}

	public static void main(String[] args) throws Exception {
		Exception foo = null;
		try {
			deep1();
		} catch (Exception e) {
			foo = e;
		}

		int i = 0;
		while (i++ < 100) {
			try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("traceId",
					UUID.randomUUID().toString())) {
				LOG.info("writing log data ... " + i);
				LOG.error("oh no!", foo);
			}
		}

		System.out.println();
	}

}
