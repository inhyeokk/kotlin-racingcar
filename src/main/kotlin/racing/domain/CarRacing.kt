package racing.domain

class CarRacing private constructor(
    private val numberStrategy: NumberStrategy,
    private val recorder: CarRacingRecordStrategy,
    private val cars: List<Car>,
) {

    init {
        require(cars.isNotEmpty()) {
            "자동차가 0대이면 자동차 경주를 할 수 없습니다."
        }
    }

    fun race(tryCount: Int): CarRacingResult {
        val results = raceWithRecordResult(cars, tryCount)
        val winners = recorder.findWinners(cars)
        return CarRacingResult(cars, results, winners)
    }

    private fun raceWithRecordResult(cars: List<Car>, tryCount: Int): List<List<Car>> {
        return List(tryCount) {
            cars.racePerRound()
            recorder.recordRacingResultPerRound(cars)
        }
    }

    private fun List<Car>.racePerRound() {
        forEach { car ->
            car.moveOrStop(numberStrategy.getNumber())
        }
    }

    companion object {
        fun of(
            numberStrategy: NumberStrategy,
            recorder: CarRacingRecordStrategy,
            carNames: List<String>,
        ): CarRacing = CarRacing(numberStrategy, recorder, carNames.map { name -> Car.of(name) })
    }
}