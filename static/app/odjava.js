Vue.component("odjava",{
    data: function(){
        return{
        }
    },
    created(){
        axios.get('/korisnici/logout', this.data)
            .then(function (response) {
                localStorage.removeItem('korisnik')
                window.location.href = "#/";
                window.location.reload();
            })
    }
});