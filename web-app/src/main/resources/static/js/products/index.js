function displayProducts() {
    fetch('http://localhost:8080/web-api/products/', {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        method: 'GET'
    })
        .then(response => response.ok ? response.json() : alert(error))
        .then(data => {
            const links = data.map(({id}) => `http://localhost:8080/products/${id}`);

            let formattedData = data.map(({name}) => `${name}`);
            formattedData = formattedData.join('-').split('-');

            let i = 0;
            formattedData.forEach(element => {
                const a = document.createElement('a');
                const link = document.createTextNode(element);
                const node = document.createElement('div');

                a.appendChild(link);
                a.title = element.toString();
                a.href = links[i];
                node.appendChild(a)
                document.querySelector('.container').append(node);
                i++;
            });
        });
}

function sortProductsByNameAsc() {
    let node = document.querySelector('.container');
    let products = node.querySelectorAll('a');

    let sortedProducts = [].map.call(products, function (product) {
        return product;
    }).sort(sortA());

    node.remove();

    node = document.createElement('div');
    node.className = 'container';

    for (let i = 0; i < sortedProducts.length; i++) {
        let divA = document.createElement('div');
        divA.appendChild(sortedProducts[i]);
        node.appendChild(divA);
    }

    document.body.appendChild(node);
}

function sortProductsByNameDesc() {
    let node = document.querySelector('.container');
    let products = node.querySelectorAll('a');

    let sortedProducts = [].map.call(products, function (product) {
        return product;
    }).sort(sortA()).reverse();

    node.remove();

    node = document.createElement('div');
    node.className = 'container';

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

displayProducts();