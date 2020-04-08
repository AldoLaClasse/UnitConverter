enum class DangerLevel (val value : Int){
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    fun getLevel () = this.value
}

fun main() {
    println(DangerLevel.HIGH.getLevel())
}