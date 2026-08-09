package businesslayer.adapter;

/**
 * Adapter Pattern (required pattern).
 * Converts a ThirdPartyDiagnosticsPacket (vendor telemetry format) into the
 * DiagnosticsReading shape MaintenanceBusinessLogic expects, so the
 * business layer never has to know about vendor-specific field names/units.
 * Object-adapter style: wraps the adaptee instance rather than extending it,
 * which keeps it usable no matter which vendor SDK class is on the classpath.
 *
 * Used by: MaintenanceBusinessLogic.ingestDiagnostics(...) (equipment
 * self-reporting real-time status, FR-03 / FR-05).
 */
public class EquipmentDiagnosticsAdapter {

    private final ThirdPartyDiagnosticsPacket packet;

    public EquipmentDiagnosticsAdapter(ThirdPartyDiagnosticsPacket packet) {
        this.packet = packet;
    }

    public DiagnosticsReading toDiagnosticsReading() {
        double hours = packet.getMinutesSinceLastPoll() / 60.0;
        boolean fault = packet.getErrorCode() != 0;
        return new DiagnosticsReading(packet.getTag(), packet.getPart(), hours, fault);
    }
}
