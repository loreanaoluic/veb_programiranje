Vue.component("registracija", {
	data: function () {
		    return {
				data : {
					pol: "MUSKO",
					datumRodjenja: "2000-01-01"
				}
		    }
	},
	template: ` 
 <div class="modal-dialog" xmlns="http://www.w3.org/1999/html">
    <div class="modal-content">
      <div class="modal-body text-start">
        <form>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.korisnickoIme" placeholder="Korisničko ime">
          </div>
          <div class="form-group">
            <input type="password" class="form-control" v-model="data.lozinka" placeholder="Lozinka">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.ime" placeholder="Ime">
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="data.prezime" placeholder="Prezime">
          </div>
          <div class="form-group">
            <div class="form-control">
				<label>Datum rođenja</label>
				<input type="date" name="datumRodjenja" v-model="data.datumRodjenja" class="labele">
            </div>
          </div>
          <div class="form-group">
            <div class="form-control">
				<label>Pol</label>
				<select name="pol" v-model="data.pol" class="labele">
				  <option value="MUSKO">MUŠKO</option>
				  <option value="ZENSKO">ŽENSKO</option>
				  <option value="DRUGO">DRUGO</option>
				</select>
			</div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="registruj()">Potvrdi</button>
      </div>
    </div>
</div>	  

`
	,
	methods : {
		init : function() {
		},
		registruj : function () {

			axios.post('/korisnici/registracija', this.data)
				.then(function (response) {
					window.location.href = "#/prijava";
					window.location.reload()
				})
				.catch(function (error) {
					alert(error.response.data);
				});
		}
	}

});
