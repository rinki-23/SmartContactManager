console.log("this is script file");

const toggleSidebar = ()=>{
	if($('.sidebar').is(":visible")){
		// if sidebar is visible then close it
		
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%")
	}else{
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%")
		
	}
};


/* search method*/ 
const search= ()=>{
	// the value is written inside search box is came into query variable
	let query = $("#search-input").val();
	// if the search box is blank then search bbox is hide

	if(query == ''){
		$(".search-result").hide();
	}
	// otherwise search box is show
	else{
		console.log(query);
		
		// send a request to server
		let url = `http://localhost:8080/search/${query}`;
		
		fetch(url).then(response=>{
			return response.json();
		}).then((data)=>{
			
			
			let text = `<div class= 'list-group'>`
			
			data.forEach(contact=>{
				text+=`<a href = '/user/${contact.cid}/contact' class = 'list-group-item list-group-item-action'>${contact.name}</a>`
			})
			text +=`</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
			
		})
		$(".search-result").show();
	}
}