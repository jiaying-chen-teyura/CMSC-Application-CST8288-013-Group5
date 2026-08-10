package businesslayer.adapter;

/**
 * Stand-in for the payload shape a third-party equipment telemetry board
 * (e.g. an OctoPrint plugin, a laser controller's diagnostics export, a CNC
 * sensor kit) actually sends: different field names/units than our domain
 * model (minutes instead of hours, "tag" instead of "assetTag",
 * "errorCode" instead of a boolean). This is the "adaptee".
 * @author Le Bao Thach Nguyen 
 */
public class ThirdPartyDiagnosticsPacket {
    private String tag;
    private String part;
    private double minutesSinceLastPoll;
    private int errorCode; // 0 = OK, non-zero = fault

    public ThirdPartyDiagnosticsPacket(String tag, String part, double minutesSinceLastPoll, int errorCode) {
        this.tag = tag;
        this.part = part;
        this.minutesSinceLastPoll = minutesSinceLastPoll;
        this.errorCode = errorCode;
    }

    public String getTag() { return tag; }
    public String getPart() { return part; }
    public double getMinutesSinceLastPoll() { return minutesSinceLastPoll; }
    public int getErrorCode() { return errorCode; }
}
