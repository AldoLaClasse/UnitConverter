class Car(val make: String, val year: Int) {

    var speed: Int = 0

   fun accelerate (){
       this.speed+=5
   }

    fun decelerate(){
        if (this.speed > 4) this.speed-=5
    }
}