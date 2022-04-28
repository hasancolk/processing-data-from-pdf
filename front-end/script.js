const userForm = document.getElementById("userForm");
const username = document.getElementById("username");
const userPassword = document.getElementById("userPassword");
const adminName = document.getElementById("adminName");
const adminPassword = document.getElementById("adminPassword");


function adminLogIn(e) {
    console.log(e);
    e.preventDefault()
    console.log(adminName.value);
    console.log(adminPassword.value);
    const data = new URLSearchParams();
    data.append("userName", adminName.value)
    data.append("password", adminPassword.value)

    fetch('http://localhost:8080/adminLogin', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => {
            console.log('Success:', cikti);
            if(cikti==-10){
                window.location.replace("http://127.0.0.1:5500/adminScreen.html");
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });

}

function userLogIn(e) {
    console.log(e);
    e.preventDefault()
    console.log(username.value);
    console.log(userPassword.value);
    const data = new URLSearchParams();
    data.append("userName", username.value)
    data.append("password", userPassword.value)

    fetch('http://localhost:8080/userLogin', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => {
            console.log('Success:', cikti);
            if(cikti>0){
                window.location.replace("http://127.0.0.1:5500/userScreen.html");
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });

}

function userSignIn(e) {
    console.log(e);
    e.preventDefault()
    console.log(username.value);
    console.log(userPassword.value);
    const data = new URLSearchParams();
    data.append("userName", username.value)
    data.append("password", userPassword.value)

    fetch('http://localhost:8080/signUser', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => {
            console.log('Success:', cikti);
            alert("Kullanıcı Sisteme Kayıt Edildi...");
        })
        .catch((error) => {
            console.error('Error:', error);
        });

}


const searchForm = document.getElementById("searchForm");
const variableSelection = document.getElementById("variableSelection");
const searchValue = document.getElementById("searchValue");


function searchData(e) {
    console.log(e);
    e.preventDefault()
    console.log(variableSelection.value);
    console.log(searchValue.value);
    const data = new URLSearchParams();
    // let headers = ['User ID', 'Name', 'Occupation', 'Age'];
    // let row = [[20012, 'Mathew', 'Tax Collector', 54],[77943, 'John', 'Fisherman', 44],[97542, 'Luke', 'Doctor', 24]];

    document.getElementById("table2").style.visibility="hidden";
    document.getElementById("table3").style.visibility="hidden";
    document.getElementById("table4").style.visibility="hidden";

    if(variableSelection.value==1){ // yazar
        data.append("nameSurnameStr", searchValue.value);
        fetch('http://localhost:8080/getProjectsForAuthor', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    else if(variableSelection.value==2){ // ders
        data.append("lesson", searchValue.value);
        fetch('http://localhost:8080/getProjectsForLesson', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    else if(variableSelection.value==3){ // proje
        data.append("projectName", searchValue.value);
        fetch('http://localhost:8080/getAuthorsForProjectName', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertDataProject(cikti))
        .then(data => loadIntoTable(data,['Ad','Soyad'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    else if(variableSelection.value==4){ // anahtar kelime
        data.append("keyWord", searchValue.value);
        fetch('http://localhost:8080/getProjectsForKeyWords', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    else if(variableSelection.value==5){ // dönem
        data.append("deliveryPeriod", searchValue.value);
        fetch('http://localhost:8080/getProjectsForDeliveryPeriod', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });
    }

}

function convertData(cikti){
    let dizi=[];
    for(let i=0;i<cikti.length;i++){
        let dizi2=[];
        dizi2.push(cikti[i].name,cikti[i].id);
        dizi.push(dizi2);
    }
    return dizi;
}

function convertDataProject(cikti){
    let dizi=[];
    for(let i=0;i<cikti.length;i++){
        let dizi2=[];
        dizi2.push(cikti[i].name,cikti[i].surname);
        dizi.push(dizi2);
    }
    return dizi;
} 


function getProjects(e) {
    console.log(e);
    e.preventDefault()

    document.getElementById("table2").style.visibility="hidden";
    document.getElementById("table3").style.visibility="hidden";
    document.getElementById("table4").style.visibility="hidden";

        fetch('http://localhost:8080/getProjects', {
        method: 'POST',
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });


}

async function loadIntoTable(rows,headers,table) {
    // const response = await fetch(url);
    // const { headers, rows } = await response.json();
    //let headers = ['Proje Adı'];
    //let rows = [["Görüntü İşleme"],["Metin İşleme"],["Yapay Zeka"]];
    console.log("headers: "+headers);
    console.log("rows: "+rows);
    table.innerHTML = `
      <thead><tr>${headers.map((itm)=>{
        return `<th>${itm}</th>`
      },"").join("")}</tr></thead>
  
      <tbody>${rows.map((itm)=>{ let sayac=0;
        return `<tr>${
          itm.map((i)=>{
              
            sayac++;
            //console.log("sayac degeri:"+sayac);
            if(sayac==2 && headers=="Proje Adı"){
                return `<td onclick="projectDetails(event,${i})">Projeye Git</td>`
            }
            
            return `<td>${i}</td>`
          },'').join("")
        }</tr>`
      },'').join('')}</tbody>
  `
  }

  function projectDetails(e,id) {
    
    console.log(e);
    e.preventDefault()
    console.log("id:"+id);
    //window.location.replace("http://127.0.0.1:5500/projectDetails.html");
    const data = new URLSearchParams();
    // let headers = ['User ID', 'Name', 'Occupation', 'Age'];
    // let row = [[20012, 'Mathew', 'Tax Collector', 54],[77943, 'John', 'Fisherman', 44],[97542, 'Luke', 'Doctor', 24]];

    document.getElementById("table2").style.visibility="visible";
    document.getElementById("table3").style.visibility="visible";
    document.getElementById("table4").style.visibility="visible";

    data.append("projectId", id);

    fetch('http://localhost:8080/getProjectFullData', {
    method: 'POST',
    body: data,
})
    .then(response => response.json())
    .then(cikti => convertDataDetails1(cikti))
    .then(data => loadIntoTable(data,['Proje Adı','Yükleyen Kullanıcı','Ders Adı','Dönem'],document.getElementById("table1")))
    .catch((error) => {
        console.error('Error:', error);
    });


    fetch('http://localhost:8080/getProjectFullData', {
    method: 'POST',
    body: data,
})
    .then(response => response.json())
    .then(cikti => convertDataDetails2(cikti))
    .then(data => loadIntoTable(data,['Yazar Ad','Soyad','Öğrenci No','Öğretim Türü'],document.getElementById("table2")))
    .catch((error) => {
        console.error('Error:', error);
    });


    fetch('http://localhost:8080/getProjectFullData', {
    method: 'POST',
    body: data,
})
    .then(response => response.json())
    .then(cikti => convertDataDetails3(cikti))
    .then(data => loadIntoTable(data,['Mentör Unvan','Ad','Soyad','Mentör Tipi'],document.getElementById("table3")))
    .catch((error) => {
        console.error('Error:', error);
    });


    fetch('http://localhost:8080/getProjectFullData', {
    method: 'POST',
    body: data,
})
    .then(response => response.json())
    .then(cikti => convertDataDetails4(cikti))
    .then(data => loadIntoTable(data,['Özet'],document.getElementById("table4")))
    .catch((error) => {
        console.error('Error:', error);
    });

}


function convertDataDetails1(cikti){ // proje ad , kullanıcı ad , dönem , ders verilerini alır.
    console.log(cikti.authors[0]);
    let dizi=[];
    let dizi2=[];
    dizi2.push(cikti.projectName,cikti.userName,cikti.lessonType,cikti.deliveryPeriod);
    dizi.push(dizi2);
    console.log(dizi);
    return dizi;
}

function convertDataDetails2(cikti){ // yazar ad , soyad , ogrenci no , ogretim turu verilerini alır.
    let dizi=[];
    for(let i=0;i<cikti.authors.length;i++){
        let dizi2=[];
        dizi2.push(cikti.authors[i].name,cikti.authors[i].surname,cikti.authors[i].studentNo,cikti.authors[i].typeEdu);
        dizi.push(dizi2);
    }
    return dizi;
}

function convertDataDetails3(cikti){ // mentor ad , soyad , unvan , mentor tipi verilerini alır.
    let dizi=[];
    for(let i=0;i<cikti.mentors.length;i++){
        let dizi2=[];
        dizi2.push(cikti.mentors[i].degree,cikti.mentors[i].name,cikti.mentors[i].surname,cikti.mentors[i].mentorType);
        dizi.push(dizi2);
    }
    return dizi;
}

function convertDataDetails4(cikti){ // ozet verisini alır.
    let dizi=[];
    let dizi2=[];
    dizi2.push(cikti.abstractForPdf);
    dizi.push(dizi2);
    return dizi;
}


const query2Form = document.getElementById("query2Form");
const searchDeliveryPeriod = document.getElementById("searchDeliveryPeriod");
const searchUsername = document.getElementById("searchUsername");
const searchLessonType = document.getElementById("searchLessonType");


function query2(e) {
    console.log(e);
    e.preventDefault()
    console.log(searchDeliveryPeriod.value);
    console.log(searchUsername.value);
    console.log(searchLessonType.value);

    document.getElementById("table2").style.visibility="hidden";
    document.getElementById("table3").style.visibility="hidden";
    document.getElementById("table4").style.visibility="hidden";

    const data = new URLSearchParams();
    // let headers = ['User ID', 'Name', 'Occupation', 'Age'];
    // let row = [[20012, 'Mathew', 'Tax Collector', 54],[77943, 'John', 'Fisherman', 44],[97542, 'Luke', 'Doctor', 24]];

    data.append("deliveryPeriod", searchDeliveryPeriod.value);
    data.append("userName", searchUsername.value);
    data.append("lessonType", searchLessonType.value);

        fetch('http://localhost:8080/getProjectForPeriodAndUserNameAndLesson', {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(cikti => convertData(cikti))
        .then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
        .catch((error) => {
            console.error('Error:', error);
        });

}


    /*var newone=[]
    var newtwo=[]
    listshow();


    function add(){

        var one=document.getElementById('one').value // kullanıcı ad
        var two=document.getElementById('two').value // şifre
        console.log("one:"+one);
        console.log("two:"+two);
        console.log();

        newone.push(one);
        newtwo.push(two);
        
        console.log("newone:"+newone);
        console.log("newtwo:"+newtwo);

        const data = new URLSearchParams();

        data.append("userName", one);
        data.append("password", two);

            fetch('http://localhost:8080/signUser', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>console.log(cikti))
            //.then(cikti => convertData(cikti))
            //.then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
            .catch((error) => {
                console.error('Error:', error);
            });

        listshow();
        location.reload();
    }


    function listshow(){
        const data = new URLSearchParams();
        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            //.then(data => listFunction(data))
            //.then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
            .catch((error) => {
                console.error('Error:', error);
            });
        
        
    }

    
    var load=""
    var oldValue;

    function edt(edit){
        load=edit;

        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            .then(data => {
                document.getElementById('one').value=data[edit*2]
                document.getElementById('two').value=data[(edit*2)+1]
                oldValue=document.getElementById('one').value;
                //console.log("document.getElementById('one').value:"+document.getElementById('one').value)
                //console.log("newtwo[edit]:"+newtwo[edit]);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
        
        console.log("edit:"+edit);
    }

    function update(){

    console.log("old value:"+oldValue);
    console.log("document.getElementById('one').value:"+document.getElementById('one').value);
    console.log("document.getElementById('two').value:"+document.getElementById('two').value);
    console.log();

    const data = new URLSearchParams();

    data.append("newUserName", document.getElementById('one').value);
    data.append("newPassword", document.getElementById('two').value);
    data.append("userName", oldValue);

    fetch('http://localhost:8080/updateUser', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>console.log("Update cikti:"+cikti))
            .catch((error) => {
                console.error('Error:', error);
            });


    listshow();
    location.reload();
    }

    function del(i){
        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            .then(data => {
                deleteUser(data[i*2]);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    
    listshow();
    location.reload();
    }

    function convertDataEnd(cikti){
        let dizi=[];
        for(let i=0;i<cikti.length;i++){
            dizi.push(cikti[i].name);
            dizi.push(cikti[i].password);
        }
        return dizi;

    }


    function listFunction(newone){
        var list=""
        for(var i=0;i<newone.length;i=i+2){
        list+= "<tr><td>"+newone[i]+"</td>"+"<td>"+newone[i+1]+"</td>"+"<td>"+"<button onclick='edt("+i/2+")'>Edit</button><button onclick='del("+i/2+")'>Delete</button>"+"</td></tr>"

        }console.log("calisti");
        document.getElementById('data').innerHTML=list
    }

    function deleteUser(userName){
    
        const data = new URLSearchParams();

        data.append("userName", userName);

        fetch('http://localhost:8080/deleteUser', {
                method: 'POST',
                body: data,
            })
                .then(response => response.json())
                .then(cikti=>console.log("Delete cikti:"+cikti))
                .catch((error) => {
                    console.error('Error:', error);
                });
    }*/

    var newone=[]
    var newtwo=[]
    listshow();


    function add(){

        var one=document.getElementById('one').value // kullanıcı ad
        var two=document.getElementById('two').value // şifre
        console.log("one:"+one);
        console.log("two:"+two);
        console.log();

        newone.push(one);
        newtwo.push(two);
        
        console.log("newone:"+newone);
        console.log("newtwo:"+newtwo);

        const data = new URLSearchParams();

        data.append("userName", one);
        data.append("password", two);

            fetch('http://localhost:8080/signUser', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>console.log(cikti))
            //.then(cikti => convertData(cikti))
            //.then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
            .catch((error) => {
                console.error('Error:', error);
            });

        listshow();
        location.reload();
    }


    function listshow(){

        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            .then(data => listFunction(data))
            //.then(data => loadIntoTable(data,['Proje Adı'],document.getElementById("table1")))
            .catch((error) => {
                console.error('Error:', error);
            });
        
        
    }

    
    var load=""
    var oldValue;

    function edt(edit){
        load=edit;

        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            .then(data => {
                document.getElementById('one').value=data[edit*2]
                document.getElementById('two').value=data[(edit*2)+1]
                oldValue=document.getElementById('one').value;
                //console.log("document.getElementById('one').value:"+document.getElementById('one').value)
                //console.log("newtwo[edit]:"+newtwo[edit]);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
        
        console.log("edit:"+edit);
    }

    function update(){

    console.log("old value:"+oldValue);
    console.log("document.getElementById('one').value:"+document.getElementById('one').value);
    console.log("document.getElementById('two').value:"+document.getElementById('two').value);
    console.log();

    const data = new URLSearchParams();

    data.append("newUserName", document.getElementById('one').value);
    data.append("newPassword", document.getElementById('two').value);
    data.append("userName", oldValue);

    fetch('http://localhost:8080/updateUser', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>console.log("Update cikti:"+cikti))
            .catch((error) => {
                console.error('Error:', error);
            });


    listshow();
    location.reload();
    }

    function del(i){
        fetch('http://localhost:8080/getUsers', {
            method: 'POST',
            body: data,
        })
            .then(response => response.json())
            .then(cikti=>convertDataEnd(cikti))
            .then(data => {
                deleteUser(data[i*2]);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    console.log(data[i*2]);
    listshow();
    location.reload();
    }

    function convertDataEnd(cikti){
        let dizi=[];
        for(let i=0;i<cikti.length;i++){
            dizi.push(cikti[i].name);
            dizi.push(cikti[i].password);
        }
        return dizi;

    }


    function listFunction(newone){
        var list=""
        for(var i=0;i<newone.length;i=i+2){
        list+= "<tr><td>"+newone[i]+"</td>"+"<td>"+newone[i+1]+"</td>"+"<td>"+"<button onclick='edt("+i/2+")'>Edit</button><button onclick='del("+i/2+")'>Delete</button>"+"</td></tr>"

        }console.log("calisti");
        document.getElementById('data').innerHTML=list
    }

    function deleteUser(userName){
    
        const data = new URLSearchParams();

        data.append("userName", userName);

        fetch('http://localhost:8080/deleteUser', {
                method: 'POST',
                body: data,
            })
                .then(response => response.json())
                .then(cikti=>console.log("Delete cikti:"+cikti))
                .catch((error) => {
                    console.error('Error:', error);
                });
    }


    