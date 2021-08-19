const ManifestacijePocetna = { template: '<pocetna-strana></pocetna-strana>' }
const Registracija = { template: '<registracija></registracija>' }
const Prijava = { template: '<prijava></prijava>' }
const Odjava = { template: '<odjava></odjava>' }
const Profil = { template: '<profil></profil>'}

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: ManifestacijePocetna},
	    { path: '/registracija', component: Registracija },
	    { path: '/prijava', component: Prijava },
	    { path: '/odjava', component: Odjava },
	    { path: '/profil', component: Profil }
	  ]
});

var app = new Vue({
	router,
	el: '#webShop'
});