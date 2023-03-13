function sortProductsByNameAsc() {
    let node = document.querySelector('.products-container');
    let products = node.querySelectorAll('a');

    let sortedProducts = [].map.call(products, function (product) {
        return product;
    }).sort(sortA());

    displaySortedProducts(node, sortedProducts);
}

function sortProductsByNameDesc() {
    let node = document.querySelector('.products-container');
    let products = node.querySelectorAll('a');

    let sortedProducts = [].map.call(products, function (product) {
        return product;
    }).sort(sortA()).reverse();

    displaySortedProducts(node, sortedProducts);
}

function displaySortedProducts(node, sortedProducts) {
    node.remove();

    node = document.createElement('div');
    node.className = 'products-container';

    for (let i = 0; i < sortedProducts.length; i++) {
        let divA = document.createElement('div');
        divA.appendChild(sortedProducts[i]);
        node.appendChild(divA);
    }

    document.body.appendChild(node);
}

function sortA() {
    return function (a, b) {
        let filter_a = a.title;
        let filter_b = b.title;
        return filter_a < filter_b
            ? -1
            : (filter_a > filter_b ? 1 : 0);
    }
}