package ie.setu.medicare.Model

data class SlotsList (
    val slotId: String,
    val slotName: String,
    var deleteSelection: Boolean,
    var editSelection: Boolean
)
