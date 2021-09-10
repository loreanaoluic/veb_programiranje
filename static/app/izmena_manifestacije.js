Vue.component("izmena-manifestacije", {
    data: function () {
        return {
            input : {
                naziv: "",
                tipManifestacije: "",
                brojMesta: "",
                datumIVremeOdrzavanja: "",
                cenaRegular: "",
                adresa: "",
                geografskaSirina: 45.2673,
                geografskaDuzina: 19.8339,
                poster: ""
            },
            manifestacija: {},
            lokacija: {
                geografskaSirina: 45.2673,
                geografskaDuzina: 19.8339
            }
        }
    },
    template: ` 
 <div class="modal-dialog" xmlns="http://www.w3.org/1999/html">
    <div class="modal-content">
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
				<label>Datum i vreme održavanja</label>
				<input type="datetime-local" name="datumIVremeOdrzavanja" v-model="input.datumIVremeOdrzavanja" class="labele">
            </div>
          </div>
          <div class="form-group">
            <input type="number" class="form-control" v-model="input.cenaRegular" placeholder="Cena regular karte">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.adresa" placeholder="Adresa">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.geografskaSirina" placeholder="Geografska širina">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.geografskaDuzina" placeholder="Geografska dužina">
          </div>
          <div id="map" class="map"></div>
           <div class="form-group">
            <div class="form-control">
                <label>Poster</label>
                <input type="file" class="labele" id="poster" name="poster" multiple v-on:change="posterDodat()">
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="potvrdi()">Izmeni</button>
      </div>
    </div>
</div>	  

`
    ,
    methods : {
        init : function() {
        },
        prikaziMapu : function(){
            let self = this;

            var vectorSource = new ol.source.Vector({});
            var vectorLayer = new ol.layer.Vector({source: vectorSource});

            var map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    }),vectorLayer
                ],
                view: new ol.View({
                    center: ol.proj.fromLonLat([19.8339, 45.2673]),
                    zoom: 11
                })
            });

            var marker;

            setMarker = function(position) {
                marker = new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat(position)));
                vectorSource.addFeature(marker);
            }

            reverseGeocode = function(coords) {
                fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
                    .then(function(response) {
                        return response.json();
                    }).then(function(json) {
                    self.input.adresa = json.address.road + " " + json.address.house_number + " " + json.address.postcode;
                });
            }

            map.on("click", function(event){
                let position = ol.proj.toLonLat(event.coordinate);
                reverseGeocode(position);
                self.input.geografskaSirina = parseFloat(position.toString().split(",")[1]).toFixed(6);
                self.input.geografskaDuzina = parseFloat(position.toString().split(",")[0]).toFixed(6);
                vectorSource.clear();
                setMarker(position);
            });

        },
        potvrdi : function() {
            let data = this;
            if (this.input.naziv === "" || this.input.tipManifestacije === "" || this.input.brojMesta === "" || this.input.brojMesta === undefined
                || this.input.datumIVremeOdrzavanja === undefined || this.input.cenaRegular === "" || this.input.adresa === ""
                || this.input.geografskaSirina === "" || this.input.geografskaDuzina === "" || this.input.poster === "") {
                alert("Ispunite ispravno sva polja!");
            }  else {
                axios.post('/manifestacije/izmenaManifestacije/' + this.manifestacija.id + "/" + this.lokacija.id, this.input)
                    .then(function (response) {
                        if (response.data === -1) {
                            alert("Odabrani datum je u prošlosti!");
                        }
                        else if (response.data === -2) {
                            alert("Već postoji manifestacija na željenoj lokaciji u to vreme!");
                        } else {
                            data.manifestacija = response.data;
                            localStorage.setItem('manifestacija', JSON.stringify(response.data));
                            window.location.href = "#/";
                            window.location.reload();
                        }
                    })
            }
        },
        posterDodat : function () {
            var name = document.getElementById('poster');
            this.input.poster = "images/manifestacije/" + name.files.item(0).name;
        }
    },
    mounted () {
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        this.lokacija = JSON.parse(localStorage.getItem('lokacija'))

        this.input.naziv = this.manifestacija.naziv;
        this.input.tipManifestacije = this.manifestacija.tipManifestacije;
        this.input.cenaRegular = this.manifestacija.cenaRegular;

        let m = this.manifestacija.datumIVremeOdrzavanja.date.month.toString();
        let d = this.manifestacija.datumIVremeOdrzavanja.date.day.toString();
        let h = this.manifestacija.datumIVremeOdrzavanja.time.hour.toString();
        let mi = this.manifestacija.datumIVremeOdrzavanja.time.minute.toString();
        if (m.length === 1) {
            m = "0" + m;
        }
        if (d.length === 1) {
            d = "0" + d;
        }
        if (h.length === 1) {
            h = "0" + h;
        }
        if (mi.length === 1) {
            mi = "0" + mi;
        }
        this.input.datumIVremeOdrzavanja = this.manifestacija.datumIVremeOdrzavanja.date.year + "-" +
            m + "-" + d + "T" + h + ":" + mi;

        this.input.brojMesta = this.manifestacija.brojMesta;
        this.input.adresa = this.lokacija.adresa;
        this.input.geografskaDuzina = this.lokacija.geografskaDuzina;
        this.input.geografskaSirina = this.lokacija.geografskaSirina;
        this.input.poster = this.manifestacija.poster;

        this.prikaziMapu();
    }

});
