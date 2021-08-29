Vue.component("pocetna-strana", {
	data: function () {
		return {
			manifestacije: [],
			searchParams: {
				naziv : "",
				lokacija: "",
				pocetniDatum: "2020-01-01",
				krajnjiDatum: "2023-01-01",
				pocetnaCena: 0,
				krajnjaCena: 1000000,
				kriterijumSortiranja: "NAZIV",
				tip: "SVE",
				opadajuce: false,
				rasprodata: false
			},
			korisnik: {}
		}
	},
	template: ` 
	<div class="container">
		<div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" v-on:click="closeNav()">&times;</a>
		  <table>
			  <tr><td style="color:white">Naziv:</td> <td><input type="text" name="naziv" v-model="searchParams.naziv" /></td></tr><br>
			  <tr><td style="color:white">Lokacija:</td> <td><input type="text" name="lokacija" v-model="searchParams.lokacija" /></td></tr><br>
			  <tr><td style="color:white">Datum od:</td> <td><input type="date" name="startDate" v-model="searchParams.pocetniDatum" ></td></tr><br>
			  <tr><td style="color:white">Datum do:</td> <td><input type="date" name="endDate" v-model="searchParams.krajnjiDatum" ></td></tr><br>
			  <tr><td style="color:white">Cena od:</td> <td><input type="number" name="startPrice" v-model="searchParams.pocetnaCena" ></td></tr><br>
			  <tr><td style="color:white">Cena do:</td> <td><input type="number" name="endPrice" v-model="searchParams.krajnjaCena" ></td></tr><br>
			  <tr><td style="color:white">Sortiraj po:</td>
			  <td>
			  <select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
			  <option value="NAZIV">NAZIV</option>
			  <option value="DATUM">DATUM I VREME</option>
			  <option value="CENA">CENA</option>
			  <option value="LOKACIJA">LOKACIJA</option>
			</select>
			</td></tr><br>
		    <tr><td style="color:white">Tip manifestacije:</td> 
			  <td>
			  <select name="tip" id="tip" v-model="searchParams.tip" >
			  <option value="KONCERT">KONCERT</option>
			  <option value="FESTIVAL">FESTIVAL</option>
			  <option value="POZORISTE">POZORIŠTE</option>
			  <option value="DOGADJAJ">DOGAĐAJ</option>
			  <option value="SVE">SVE</option>
			</select>
			</td></tr><br>
			  <tr><td style="color:white">Opadajući prikaz:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <br>
			  <tr><td style="color:white">Prikaži rasprodate:</td> <td><input type="checkbox" name="rasprodate" v-model="searchParams.rasprodata" ></td></tr>
			  <br><br><br>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Potvrdi" v-on:click="pretrazi()" /></td></tr>
		  </table>
	</div>
    <div class="main">  
      <button class="openbtn" v-on:click="openNav()">&#9776; Filter</button>
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
                          <p><button type="button" class="btn btn-info" v-on:click="prikaziInformacije(manifestacija)">Više informacija</button></p>
                          <div v-if="(korisnik !== null)">
						  	<div v-if="(korisnik.uloga === 'ADMIN')">
								<div class="modal-footer">
									<button type="button" class="btn btn-info" v-on:click="prikaziKarte(manifestacija)">Karte</button>
								</div>
							</div>
						  </div>
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
		this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
		let data = this;
		axios.get('/manifestacije', this.data)
			.then(function (response) {
				data.manifestacije = response.data;
			})
	},
	methods : {
		init : function() {
		},
		prikaziInformacije : function (manifestacija) {
			localStorage.setItem('manifestacija', JSON.stringify(manifestacija))
			window.location.href = "#/manifestacija";
		},
		pretrazi : function () {
			let self = this;
			let pDatum = (new Date(this.searchParams.pocetniDatum)).getTime();
			let kDatum = (new Date(this.searchParams.krajnjiDatum)).getTime();

			axios.post('/manifestacije/pretraga?naziv='  + this.searchParams.naziv + "&pocetniDatum=" + pDatum + "&krajnjiDatum="
				+ kDatum + "&lokacija=" + this.searchParams.lokacija + "&pocetnaCena=" + this.searchParams.pocetnaCena +
				"&krajnjaCena=" + this.searchParams.krajnjaCena + "&tip=" + this.searchParams.tip + "&rasprodata=" +
				this.searchParams.rasprodata + "&kriterijumSortiranja=" + this.searchParams.kriterijumSortiranja +
				"&opadajuce=" + this.searchParams.opadajuce)
				.then(function (response) {
					self.manifestacije = response.data;

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