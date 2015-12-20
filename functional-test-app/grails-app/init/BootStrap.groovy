import test.TestDataService

class BootStrap {

	TestDataService testDataService

	def init = {
		testDataService.createData()
	}
}
