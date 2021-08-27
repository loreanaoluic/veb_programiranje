Vue.component("manifestacija", {
    data: function () {
        return {
            manifestacija: {},
            lokacija: {},
            aktivna: "",
            mapPosition: {
                latitude: 45.267136,
                longitude: 19.833549
            }
        }
    },
    template: ` 
 <div class="modal-dialog" xmlns="http://www.w3.org/1999/html">
    <div class="modal-content">
      <div class="modal-body text-start">
        <form>
          <div class="d-flex flex-column align-items-center text-center">
              <img  v-bind:src="manifestacija.poster" alt="Avatar" style="width:100%">
              <h4 class="d-flex align-items-center mb-3"> <b> {{ manifestacija.naziv }} </b></h4>
              <i class="material-icons text-info mr-2">{{ manifestacija.tipManifestacije }} </i>
              <br>
              <p>Datum i vreme održavanja: {{ manifestacija.datumIVremeOdrzavanja.date.day }}.{{ manifestacija.datumIVremeOdrzavanja.date.month }}.{{ manifestacija.datumIVremeOdrzavanja.date.year }}. u 
                  {{ manifestacija.datumIVremeOdrzavanja.time.hour}}:{{ manifestacija.datumIVremeOdrzavanja.time.minute}}</p>
              <p><i>REGULAR karta: {{ manifestacija.cenaRegular }} din</i></p>
              <p><i>Lokacija: {{ lokacija.adresa }} </i></p>
              <p><b>Preostalo {{ manifestacija.brojMesta }} karata! </b></p>
              <p><i>Status: {{ aktivna }} </i></p>
          </div>
        </form>
      </div>
    </div>
    <div class="col map" id="map" >
        </div>
</div>	  

`
    ,
    mounted(){
        let data = this;
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        axios.get('/manifestacije/' + this.manifestacija.id, this.data)
            .then(function (response) {
                data.lokacija = response.data;
                data.prikaziMapu();
            })
        if (this.manifestacija.status === true) {
            this.aktivna = "AKTIVNA";
        } else {
            this.aktivna = "NEAKTIVNA";
        }
    }
    ,
    methods : {
        prikaziMapu : function(){
            let self = this;
            self.mapPosition.latitude = parseFloat(this.lokacija.geografskaSirina);
            self.mapPosition.longitude = parseFloat(this.lokacija.geografskaDuzina);
            self.lokacija.geografskaSirina = self.mapPosition.latitude;
            self.lokacija.geografskaDuzina = self.mapPosition.longitude;

            console.log(self.mapPosition.latitude);
            console.log(self.mapPosition.longitude);
            let map = new ol.Map({
                target: 'map',
                interactions: [],
                controls: [],
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: ol.proj.fromLonLat([self.mapPosition.longitude, self.mapPosition.latitude]),
                    zoom: 17
                })
            });
        }
    }

});
