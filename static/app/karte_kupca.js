Vue.component("karte-kupca", {
    data: function () {
        return {
            korisnik: {},
            karte: [],
            manifestacije: {},
            searchParams: {
                naziv : "",
                pocetniDatum: "2020-01-01",
                krajnjiDatum: "2023-01-01",
                pocetnaCena: 0,
                krajnjaCena: 1000000,
                kriterijumSortiranja: "MANIFESTACIJA",
                tip: "SVE",
                status: "SVE",
                opadajuce: false
            }
        }
    },
    template: `  
  <div class="container">
    <div class="main-body">
          <button class="openbtn" v-on:click="openNav()">&#9776; Filter</button>
          <div class="row gutters-sm">
            <div class="col-sm-5 mb-3" v-for="karta in karte" :key="karta.id">
                <div class="karte">
                    <div class="card-body">
                          <div class="d-flex flex-column align-items-center text-center"><br>
                          <b class="material-icons text-info mr-2"> {{ manifestacije[karta.manifestacija].naziv }} </b>  
                          <i>Datum i vreme održavanja: {{ karta.datumIVremeManifestacije.date.day }}.{{ karta.datumIVremeManifestacije.date.month }}.{{ karta.datumIVremeManifestacije.date.year }}. u 
                              {{ karta.datumIVremeManifestacije.time.hour}}:{{ karta.datumIVremeManifestacije.time.minute }} </i>
                          <i> Status: {{ karta.statusKarte }}</i>
                          <b> Cena: {{ karta.cena }} </b>
                          <i style="font-size:12px"> Tip karte: {{ karta.tipKarte }}</i>
                          <i class="material-icons text-info mr-2"> Prodavac: {{ karta.prodavac }} </i><br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" v-on:click="closeNav()">&times;</a>
		  <table>
			  <tr><td style="color:white">Manifestacija:</td> <td><input type="text" name="naziv" v-model="searchParams.naziv" /></td></tr><br>
			  <tr><td style="color:white">Datum od:</td> <td><input type="date" name="startDate" v-model="searchParams.pocetniDatum" ></td></tr><br>
			  <tr><td style="color:white">Datum do:</td> <td><input type="date" name="endDate" v-model="searchParams.krajnjiDatum" ></td></tr><br>
			  <tr><td style="color:white">Cena od:</td> <td><input type="number" name="startPrice" v-model="searchParams.pocetnaCena" ></td></tr><br>
			  <tr><td style="color:white">Cena do:</td> <td><input type="number" name="endPrice" v-model="searchParams.krajnjaCena" ></td></tr><br>
			  <tr><td style="color:white">Sortiraj po:</td>
			  <td>
			  <select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
			  <option value="MANIFESTACIJA">MANIFESTACIJA</option>
			  <option value="DATUM I VREME">DATUM I VREME</option>
			  <option value="CENA">CENA</option>
			</select>
			</td></tr><br>
		    <tr><td style="color:white">Tip karte:</td> 
			  <td>
			  <select name="tip" id="tip" v-model="searchParams.tip" >
			  <option value="VIP">VIP</option>
			  <option value="REGULAR">REGULAR</option>
			  <option value="FAN_PIT">FAN PIT</option>
			  <option value="SVE">SVE</option>
			</select>
			</td></tr><br>
            <tr><td style="color:white">Status karte:</td> 
			  <td>
			  <select name="status" id="status" v-model="searchParams.status" >
			  <option value="REZERVISANA">REZERVISANA</option>
			  <option value="ODUSTANAK">ODUSTANAK</option>
			  <option value="SVE">SVE</option>
			</select>
			</td></tr><br>
			  <tr><td style="color:white">Opadajući prikaz:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <br><br><br>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Potvrdi" v-on:click="pretrazi()" /></td></tr>
		  </table>
	</div>
</div>
`
    ,
    mounted(){
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
        let data = this;
        axios.get('/karteKupca', this.data)
            .then(function (response) {
                data.karte = response.data;
            })
        axios.get('/manifestacijeMapa', this.data)
            .then(function (response) {
                data.manifestacije = response.data;
            })
    },
    methods : {
        init : function() {
        },
        pretrazi : function () {
            let self = this;
            let pDatum = (new Date(this.searchParams.pocetniDatum)).getTime();
            let kDatum = (new Date(this.searchParams.krajnjiDatum)).getTime();

            axios.post('/karte/pretraga?naziv='  + this.searchParams.naziv + "&pocetniDatum=" + pDatum +
                "&krajnjiDatum=" + kDatum + "&pocetnaCena=" + this.searchParams.pocetnaCena +
                "&krajnjaCena=" + this.searchParams.krajnjaCena + "&tip=" + this.searchParams.tip + "&status=" +
                this.searchParams.status + "&kriterijumSortiranja=" + this.searchParams.kriterijumSortiranja +
                "&opadajuce=" + this.searchParams.opadajuce)
                .then(function (response) {
                    self.karte = response.data;

                })
                .catch(function (error) {
                    alert(error.response.data);
                });
        },
        openNav : function () {
            document.getElementById("mySidebar").style.width = "330px";
        },

        closeNav : function () {
            document.getElementById("mySidebar").style.width = "0";
        }
    }

});
