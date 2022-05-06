fetch('http://localhost:8080/web-api/products/', {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    method: 'GET'
})
    .then(response => response.ok ? response.json() : alert(error))
    .then(data => {
        const links = data.map(({id}) => `http://localhost:8080/web-api/products/${id}`);

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