
data class Res(val results: Array<ResultObj>, val status: String)
data class ResultObj(val address_components: Array<AddressComponents>, val formatted_address: String)
data class AddressComponents(val long_name: String, val short_name: String, val types: Array<String>)
