package businesslayer.adapter;

/**
 * The shape OUR application wants for a diagnostics reading, regardless of
 * which physical equipment/telemetry vendor produced it. This is the
 * "target interface" the Adapter pattern converts everything into.
 * @author Le Bao Thach Nguyen 
 */
public class DiagnosticsReading {
    private final String assetTag;
    private final String componentName;
    private final double additionalUsageHours;
    private final boolean faultDetected;

    public DiagnosticsReading(String assetTag, String componentName, double additionalUsageHours, boolean faultDetected) {
        this.assetTag = assetTag;
        this.componentName = componentName;
        this.additionalUsageHours = additionalUsageHours;
        this.faultDetected = faultDetected;
    }

    public String getAssetTag() { return assetTag; }
    public String getComponentName() { return componentName; }
    public double getAdditionalUsageHours() { return additionalUsageHours; }
    public boolean isFaultDetected() { return faultDetected; }
}
