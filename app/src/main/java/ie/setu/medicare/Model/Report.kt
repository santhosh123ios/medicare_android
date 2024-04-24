package ie.setu.medicare.Model

data class Report(
    val rpId: String,
    val rpDate: String,
    val apSlot: String,
    val apSlotId: String,
    val ptName: String,
    val ptId: String,
    val drName: String,
    val drId: String,
    val report: String
)