const toBase64 = file => new Promise((resolve, reject) => {
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = () => resolve(reader.result);
  reader.onerror = error => reject(error);
});

async function uploadFile() {
  

  const file = document.querySelector('#pdf-file').files[0];
  var formData = new FormData();
  formData.append('pdf-file', file);

  $.ajax({
    url : 'http://localhost:8080/uploadFile',
    type : 'POST',
    data : formData,
    processData: false,
    contentType: false,
    success : function(data) {
      console.log("hasan");
      alert(data);
    }
  });
}



async function upload2File() {
  


  $.ajax({
    url : 'http://localhost:8080/getAuthors',
    type : 'GET',
    contentType: 'json',
    success : function(data) {
      console.log("hasan");
      alert(data);
    }
  });
}
