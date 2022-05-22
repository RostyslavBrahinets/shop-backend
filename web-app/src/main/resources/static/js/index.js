fetch('http://localhost:8080/web-api/', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
})
    .then(response => response.json())
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
            document.querySelector('.products-container').append(node);
            i++;
        })
    })
    .catch(error => console.log('Request failed', error));