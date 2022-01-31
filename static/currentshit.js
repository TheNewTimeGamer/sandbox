let skip = 0;

const categoryElements = document.getElementsByClassName("category");
const categories = {};
for(const category of categoryElements){
    categories[category.innerText] = {dom: category, enabled: true};
}

async function fetchInitial() {
    const response = await fetch("/api/posts?skip=" + skip + "&limit=10&tags=" + Object.keys(categories).filter(key => categories[key].enabled).join(",").toLowerCase());
    skip += 10;
    return response;
}

async function fetchNextPage() {
    const response = await fetch("/api/posts?skip=" + skip + "&limit=3&tags=" + Object.keys(categories).filter(key => categories[key].enabled).join(",").toLowerCase());
    skip += 3;
    return response;
}

async function fetchNextMeme() {
    return await fetch("/api/posts?skip=" + skip++ + "&limit=1&tags=" + Object.keys(categories).filter(key => categories[key].enabled).join(",").toLowerCase());
}

function toggleCategory(e) {
    const category = categories[e.innerText];
    category.enabled = !category.enabled;
    if(category.enabled){
        category.dom.classList.remove("bar-mid-color");
        category.dom.classList.add("bar-light-color");
    }else{
        category.dom.classList.remove("bar-light-color");
        category.dom.classList.add("bar-mid-color");
    }
}

fetchInitial().then(response => response.json()).then(posts => {
    console.log(posts);
    for(const post of posts){

    }
});


function showMemes(json) {
    console.log(json);
}