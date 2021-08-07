const ManifestacijePocetna = { template: '<pocetna-strana></pocetna-strana>' }
const Registracija = { template: '<registracija></registracija>' }

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: ManifestacijePocetna},
	    { path: '/registracija', component: Registracija }
	  ]
});


var app = new Vue({
	router,
	el: '#webShop'
});