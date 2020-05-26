import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class Examples {
    @Before
    fun setup() {
        println()
        println("Start of Test")
        println()
    }

    @After
    fun teardown() {
        println()
        println("End of Test")
        println()

    }


    @Test
    fun test1() {
        Observable.just(1, 2, 3, 4)
            .subscribe { println(it) }
    }


    @Test
    fun test2() {
        Observable.just(1, 2, 3, 4)
            .subscribeOn(Schedulers.io())
            .subscribe { println(it) }
    }

    @Test
    fun test3() {
        Observable.just(1, 2, 3, 4)
            .map {
                println("First map will double $it")
                it * 2
            }
            .map {
                println("Second map will square $it")
                it * it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnComplete { println("Complete") }
            .subscribe { println("Result: $it") }
        Thread.sleep(1000)
    }

    @Test
    fun `test use interval`() {
        Observable.interval(10, TimeUnit.MILLISECONDS)
            .take(5)
            .subscribe { println(it) }
    }

    @Test
    fun `test flatMap`() {
        Observable.just(1, 2, 3, 4, 5, 6)
            .flatMap { Observable.just("Number $it", "Square ${it * it}").delay(200, TimeUnit.MILLISECONDS) }
            .subscribeOn(Schedulers.computation())
            .subscribe { println(it) }
        Thread.sleep(4000)
    }

    private fun println(value: Any) {
        val str = "${Thread.currentThread().name}\t$value"

        val newValue = str
            .replace("RxCachedThreadScheduler", "         IO")
            .replace("RxComputationThreadPool", "COMPUTATION")
        System.out.println(newValue)
    }
}