package org.chorusbdd.chorus.handlers.remoting;

import org.chorusbdd.chorus.ChorusException;
import org.chorusbdd.chorus.handlers.util.config.AbstractHandlerConfigBuilder;
import org.chorusbdd.chorus.handlers.util.config.HandlerConfigBuilder;
import org.chorusbdd.chorus.util.logging.ChorusLog;
import org.chorusbdd.chorus.util.logging.ChorusLogFactory;

import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 21/09/12
 * Time: 08:51
 */
public class RemotingConfigBuilder extends AbstractHandlerConfigBuilder implements HandlerConfigBuilder<RemotingConfig> {

    private static ChorusLog log = ChorusLogFactory.getLog(RemotingConfigBuilder.class);

    public RemotingConfig createConfig(Properties p) {

        RemotingConfig r = new RemotingConfig();
        for (Map.Entry prop : p.entrySet()) {
            String key = prop.getKey().toString();
            String value = prop.getValue().toString();

            if ("name".equals(key)) {
                r.setName(value);
            } else if ("protocol".equals(key)) {
                r.setProtocol(value);
            } else if ("host".equals(key)) {
                r.setHost(value);
            } else if ("port".equals(key)) {
                r.setPort(parseIntProperty(value, "port"));
            } else if ("connectionAttempts".equals(key)) {
                r.setConnnectionAttempts(parseIntProperty(value, "connectionAttempts"));
            } else if ("connectionAttemptsMillis".equals(key)) {
                r.setConnectionAttemptMillis(parseIntProperty(value, "connectionAttemptsMillis"));
            } else if ( "connection".equals(key)) {
                String[] vals = String.valueOf(value).split(":");
                if (vals.length != 3) {
                    throw new ChorusException(
                        "Could not parse remoting property 'connection', " +
                        "expecting a value in the form protocol:host:port"
                    );
                }
                r.setProtocol(vals[0]);
                r.setHost(vals[1]);
                r.setPort(parseIntProperty(vals[2], "connection:port"));
            }
        }
        return r;
    }

}