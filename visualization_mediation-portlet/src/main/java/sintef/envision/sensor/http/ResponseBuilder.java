package sintef.envision.sensor.http;


/**
 * Builds a SOAP response for a notification.
 * 
 * @author Thomas Everding
 *
 */
public class ResponseBuilder {
	
	private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><soap:Header>";
	private static final String TO_OPEN = "<wsa:To>";
	private static final String ADDRESS_OPEN = "<wsa:Address>";
	private static final String TO = "http://localhost:8080/ses-main-3.0-SNAPSHOT/services/SesPortType";
	private static final String  ADDRESS_CLOSE = "</wsa:Address>";
	private static final String TO_CLOSE = "</wsa:To>";
	private static final String ACTION_ID = "<wsa:Action>http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/NotifyResponse</wsa:Action>";
	//RelatesTo optional?
	private static final String FROM = "<wsa:From>http://www.w3.org/2005/08/addressing/role/anonymous</wsa:From>";
	private static final String HEAD_BODY = "</soap:Header><soap:Body>";
	private static final String BODY = "<wsnt:NotifyResponse xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\"/>";
	private static final String END = "</soap:Body></soap:Envelope>";
	
	private static final String ANONYMOUS = "http://www.w3.org/2005/08/addressing/role/anonymous";
	
	/**
	 * 
	 * @return a SOAP / WS-N notify response
	 */
	public static String buildResponse() {
		return START + TO_OPEN + ADDRESS_OPEN +  TO + ADDRESS_CLOSE + TO_CLOSE + ACTION_ID + FROM + HEAD_BODY + BODY + END;
	}


	/**
	 * 
	 * @param to TO address for the SOAP header
	 * 
	 * @return a SOAP / WS-N notify response
	 */
	public static String buildResponse(String to) {
		return START + TO_OPEN + ADDRESS_OPEN + to + ADDRESS_CLOSE + TO_CLOSE + ACTION_ID + FROM + HEAD_BODY + BODY + END;
	}
	
	
	/**
	 * 
	 * @return a SOAP / WS-N notify response to anonymous
	 */
	public static String buildResponseToAnonymous() {
		return START + TO_OPEN + ANONYMOUS + TO_CLOSE + ACTION_ID + FROM + HEAD_BODY + BODY + END;
	}
}

/* Example sent by SES:
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
    <soap:Header>
        <wsa:To>http://www.w3.org/2005/08/addressing/role/anonymous</wsa:To>
        <wsa:Action>http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/NotifyResponse</wsa:Action>
        <wsa:MessageID>uuid:94947dee-e504-a01e-170a-48e1af78e276</wsa:MessageID>
        <wsa:RelatesTo RelationshipType="wsa:Reply">uuid:1b4d3025-f80a-a5b6-aa37-864c47fa1a7e</wsa:RelatesTo>
        <wsa:From>
            <wsa:Address>http://localhost:8080/ses-main-3.0-SNAPSHOT/services/SesPortType</wsa:Address>
        </wsa:From>
    </soap:Header>
    <soap:Body>
        <wsnt:NotifyResponse xmlns:wsnt="http://docs.oasis-open.org/wsn/b-2"/>
    </soap:Body>
</soap:Envelope>
*/