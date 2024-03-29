Vue.component("prijava", {
    data: function () {
        return {
            data : {
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
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="prijavi()">Prijavi se</button>
      </div>
    </div>
</div>	  

`
    ,
    methods : {
        init : function() {
        },
        prijavi : function () {
            axios.post('/korisnici/login', this.data)
                .then(function (response) {
                    if (response.data === "Greska") {
                        alert("Pogrešno korisničko ime/lozinka.");

                    } else if (response.data === "Nalog blokiran!") {
                        alert(response.data);
                    } else if (response.data === "Nalog obrisan!") {
                        alert(response.data);
                    } else {
                        localStorage.setItem('korisnik', JSON.stringify(response.data));
                        window.location.href = "#/";
                        window.location.reload();
                    }
                })
                .catch(function (error) {
                    alert("Pogrešno korisničko ime/lozinka.");
            });
        }
    }

});
