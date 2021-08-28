Vue.component("manifestacija", {
    data: function () {
        return {
            manifestacija: {},
            aktivna: "",
            mapPosition: {
                latitude: 45.267136,
                longitude: 19.833549
            },
            korisnik: {},
            input: {},
            lokacija: {},
            poster: "",
            novaLokacija: {}
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
      <div v-if="(korisnik !== null)">
        <div v-if="(korisnik.uloga === 'ADMIN')">
            <div v-if="(aktivna === 'NEAKTIVNA')">
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" v-on:click="aktivno()">Odobri manifestaciju</button>
                </div>
            </div>
        </div>
         <div v-if="(korisnik.uloga === 'PRODAVAC')">
            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#IzmeniModal">Izmeni</button>
            </div>
        </div>
    </div>
    <div class="col map" id="map">
    </div>
    
    <div class="modal fade" id="IzmeniModal" tabindex="-1" aria-labelledby="IzmeniModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="IzmeniModal">Izmeni manifestaciju</h5>
              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="modal-body text-start">
                <form>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.naziv" placeholder="Naziv">
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.tipManifestacije" placeholder="Tip manifestacije">
                  </div>
                  <div class="form-group">
                    <input type="number" class="form-control" v-model="input.brojMesta" placeholder="Broj mesta">
                  </div>
                  <div class="form-group">
                    <div class="form-control">
                        <label>Datum i vreme</label>
                        <input type="datetime-local" name="datumIVremeOdrzavanja" v-model="input.datumIVremeOdrzavanja" class="labele">
                    </div>
                  </div>
                   <div class="form-group">
                    <input type="number" class="form-control" v-model="input.cenaRegular" placeholder="Cena regular karte">
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="novaLokacija.adresa" placeholder="Adresa">
                  </div>
                   <div class="form-group">
                    <div class="form-control">
                        <label>Poster</label>
                        <input type="file" class="labele" id="poster" name="poster" multiple v-on:change="posterDodat()">
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="potvrdi">Potvrdi</button>
              </div>
            </div>
          </div>
        </div>
      </div>
  </div>
</div>	  

`
    ,
    mounted() {
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
        let data = this;
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        axios.get('/manifestacije/' + this.manifestacija.id, this.data)
            .then(function (response) {
                data.lokacija = response.data;
                data.novaLokacija.adresa = data.lokacija.adresa;
                data.novaLokacija.geografskaSirina = data.lokacija.geografskaSirina;
                data.novaLokacija.geografskaDuzina = data.lokacija.geografskaDuzina;
                data.prikaziMapu();
            })
        if (this.manifestacija.status === true) {
            this.aktivna = "AKTIVNA";
        } else {
            this.aktivna = "NEAKTIVNA";
        }
        this.input.naziv = this.manifestacija.naziv;
        this.input.tipManifestacije = this.manifestacija.tipManifestacije;
        this.input.brojMesta = this.manifestacija.brojMesta;
        this.input.datumIVremeOdrzavanja = this.manifestacija.datumIVremeOdrzavanja;
        this.input.cenaRegular = this.manifestacija.cenaRegular;
        this.input.poster = this.manifestacija.poster;
    }
    ,
    methods : {
        prikaziMapu : function() {
            let self = this;
            self.mapPosition.latitude = parseFloat(this.lokacija.geografskaSirina);
            self.mapPosition.longitude = parseFloat(this.lokacija.geografskaDuzina);
            self.lokacija.geografskaSirina = self.mapPosition.latitude;
            self.lokacija.geografskaDuzina = self.mapPosition.longitude;

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
        },
        aktivno : function() {
            this.aktivna = "AKTIVNA";
            let data = this;
            axios.post('/manifestacije/status/' + this.manifestacija.id, this.data)
                .then(function (response) {
                    data.manifestacija = response.data;
                })
        },
        posterDodat : function() {
            var name = document.getElementById('poster');
            this.input.poster = "images/manifestacije/" + name.files.item(0).name;
        },
        potvrdi : function() {
            let data = this;
            if (this.input.naziv === "" || this.input.tipManifestacije === "" || this.input.brojMesta === ""
                || this.input.datumIVremeOdrzavanja === null || this.input.cenaRegular === "" || this.input.adresa === ""
                || this.input.geografskaSirina === "" || this.input.geografskaDuzina === "" || this.input.poster === "") {
                alert("Ispunite ispravno sva polja!");
            } else {
                axios.post('/manifestacije/izmenaManifestacije/' + this.manifestacija.id, this.input)
                    .then(function (response) {
                        data.manifestacija = response.data;
                        window.location.href = "#/";
                        window.location.reload();
                    })
            }
        }
    }

});
