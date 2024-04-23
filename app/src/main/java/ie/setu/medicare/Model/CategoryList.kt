package ie.setu.medicare.Model

data class CategoryList (
    val catId: String,
    val catName: String,
    var deleteSelection: Boolean,
    var editSelection: Boolean
)