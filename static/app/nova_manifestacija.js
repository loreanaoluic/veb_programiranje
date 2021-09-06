Vue.component("nova-manifestacija", {
    data: function () {
        return {
            data : {
                naziv: "",
                tipManifestacije: "",
                brojMesta: "",
                datumIVremeOdrzavanja: "",
                cenaRegular: "",
                adresa: "",
                geografskaSirina: 45.2673,
                geografskaDuzina: 19.8339,
                poster: ""
            }
        }
    },
    template: ` 
 <div class="modal-dialog" xmlns="http://www.w3.org/1999/html">
    <div class="modal-content">
      <div class="modal-body text-start">
        <form>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.naziv" placeholder="Naziv">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.tipManifestacije" placeholder="Tip manifestacije">
          </div>
          <div class="form-group">
            <input type="number" class="form-control" v-model="data.brojMesta" placeholder="Broj mesta">
          </div>
          <div class="form-group">
            <div class="form-control">
				<label>Datum i vreme održavanja</label>
				<input type="datetime-local" name="datumIVremeOdrzavanja" v-model="data.datumIVremeOdrzavanja" class="labele">
            </div>
          </div>
          <div class="form-group">
            <input type="number" class="form-control" v-model="data.cenaRegular" placeholder="Cena regular karte">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.adresa" placeholder="Adresa">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.geografskaSirina" placeholder="Geografska širina">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.geografskaDuzina" placeholder="Geografska dužina">
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
        <button type="button" class="btn btn-primary" v-on:click="potvrdi()">Potvrdi</button>
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
                    self.data.adresa = json.address.road + " " + json.address.house_number + " " + json.address.postcode;
                });
            }

            map.on("click", function(event){
                let position = ol.proj.toLonLat(event.coordinate);
                reverseGeocode(position);
                self.data.geografskaSirina = parseFloat(position.toString().split(",")[1]).toFixed(6);
                self.data.geografskaDuzina = parseFloat(position.toString().split(",")[0]).toFixed(6);
                vectorSource.clear();
                setMarker(position);
            });

        },
        potvrdi : function () {
            if (this.data.naziv === "" || this.data.tipManifestacije === "" || this.data.brojMesta === ""
                || this.data.datumIVremeOdrzavanja === "" || this.data.cenaRegular === "" || this.data.adresa === ""
                || this.data.geografskaSirina === "" || this.data.geografskaDuzina === "" || this.data.poster === "") {
                alert("Ispunite ispravno sva polja!");
            } else {
                axios.post('/manifestacije/nova', this.data)
                    .then(function (response) {
                        if (response.data === "Done") {
                            window.location.href = "#/manifestacije-prodavca";
                        } else {
                            alert(response.data);
                        }
                    })
            }
        },
        posterDodat : function () {
            var name = document.getElementById('poster');
            this.data.poster = "images/manifestacije/" + name.files.item(0).name;
        }
    },
    mounted () {
        this.prikaziMapu();
    }

});
