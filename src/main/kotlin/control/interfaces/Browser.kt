package control.interfaces

interface Browser {

    fun newTab(url: String, active: Boolean = true): Tab
    fun getCurrentTab(): Tab
    fun getTabs(): List<Tab>
    fun onTabCreated(block: (Tab) -> Unit)
    fun onTabUpdated(block: (Tab) -> Unit)

}