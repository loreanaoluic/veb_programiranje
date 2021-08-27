Vue.component("manifestacije-prodavca", {
    data: function () {
        return {
            manifestacije: []
        }
    },
    template: ` 
<div class="container">
    <div class="main-body">  
          <div class="row gutters-sm">
            <div class="col-sm-5 mb-3" v-for="manifestacija in manifestacije" :key="manifestacija.id">
                <div class="card">
                    <div class="card-body">
                          <div class="d-flex flex-column align-items-center text-center">
                          <img  v-bind:src="manifestacija.poster" alt="Avatar" style="width:100%">
                          <h4 class="d-flex align-items-center mb-3"> <b> {{ manifestacija.naziv }} </b></h4>
                          <i class="material-icons text-info mr-2">{{ manifestacija.tipManifestacije }} </i>
                          <br>
                          <p>Datum i vreme održavanja: {{ manifestacija.datumIVremeOdrzavanja.date.day }}.{{ manifestacija.datumIVremeOdrzavanja.date.month }}.{{ manifestacija.datumIVremeOdrzavanja.date.year }}. u 
                              {{ manifestacija.datumIVremeOdrzavanja.time.hour}}:{{ manifestacija.datumIVremeOdrzavanja.time.minute}}</p>
                          <p><i>REGULAR karta: {{ manifestacija.cenaRegular }} din</i></p>
                          <p><button type="button" class="btn btn-info" v-on:click="prikaziKarte(manifestacija)">Karte</button></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

`
    ,
    mounted(){
        let data = this;
        axios.get('/manifestacije/manifestacije-prodavca', this.data)
            .then(function (response) {
                data.manifestacije = response.data;
            })
    },
    methods : {
        init : function() {
        },
        prikaziKarte : function (manifestacija) {
            axios.get('/karte')
                .then(function (response) {
                    localStorage.setItem('manifestacija', JSON.stringify(manifestacija))
                    window.location.href = "#/karte-za-manifestaciju";
                })
        }
    }

});