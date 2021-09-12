const categoryElements = document.getElementsByClassName("category");
const categories = {};
for(const category of categoryElements){
    categories[category.innerText] = {dom: category, enabled: false};
}

function fetchNextPage() {
    
}

function fetchNextMeme() {
    
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