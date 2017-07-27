package test

class BootStrap {

	TestDataService testDataService

	def init = {
		testDataService.createData()
	}
}
