const admin = require('firebase-admin');
const express = require('express');
const app = express();
const fetch = require('node-fetch');
const serviceAccount = require('./serviceAccountKey.json');

var CurrencyCollection;

/**
 * these two lines is for post request, needed to read the data from post body
 */
app.use(express.urlencoded({extended:true}));
app.use(express.json());

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

let url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=392aff9e-3b9d-46df-b8a5-1364a0864ae4";
let newurl = "https://min-api.cryptocompare.com/data/top/totalvolfull?limit=30&tsym=USD&api_key=50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a";
let url3 = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&tsym=USD&limit=3&api_key=50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a"
let url_hours = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&aggregate=3&tsym=USD&limit=3&api_key=50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a";
const db = admin.firestore();




/**
 * gets data of a currency both 15 minutes and 3 hours
 * @param {*} name 
 */
function get_SingleData_From_Api(name){
  getSingleCrytData_15_min(name);
  //getSingleCrytData_3_hour(name);
}

/**
 * calling the list from database so result for each coin is generated
 */
async function get_AllData_From_API(){
  var coinList = await admin.firestore().collection('Top30').get();
  for(var i = 0; i < coinList.docs.length; i++){
    var symbol = coinList.docs[i].data().symbol;
    get_SingleData_From_Api(symbol);
  }
  
  var allcoin = "";
  allcoin = coinList.docs[0].data().symbol;
  for(var i = 1; i < coinList.docs.length; i++){
    allcoin += ",";
    allcoin += coinList.docs[i].data().symbol;
  }
  getCurrentCrtyData(allcoin);
  
}

/**
 * getting data of a crytocurrency which is 15 minutes apart
 * @param {*} symbol 
 */
function getSingleCrytData_15_min(symbol){
  var url_Data_15 = "https://min-api.cryptocompare.com/data/v2/histominute?fsym=XXX&aggregate=15&tsym=USD&limit=99&api_key=50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a";
  var newurl_data = url_Data_15.replace('XXX', symbol);
  fetch(newurl_data).then(function(response){
    return response.json();
  }).then(async function(data){
        var Obj = {};
        var Obj2 = [];
        var size = data.Data.Data
        for(var i = 0; i< data.Data.Data.length; i++){
          var timeObj = {};

          timeObj.price = data.Data.Data[i].open;
          timeObj.time = data.Data.Data[i].time;
          Obj2.push(timeObj);
        }

        var timeData = data.Data;
        Obj.fifthteen_minutes = Obj2; 
        await admin.firestore().collection("CoinData").doc(symbol).set(Obj, {merge: true});
  })
      //console.log("put in");
}

/**
 * getting data of a single crytocurrency which values are 3 hours apart 
 * @param {*} symbol 
 */
function getSingleCrytData_3_hour(symbol){
  var url_Data_3h = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=XXX&tsym=USD&aggregate=3&limit=250&api_key=50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a";
  var newurl_data = url_Data_3h.replace('XXX', symbol);
  fetch(newurl_data).then(function(response){
    return response.json();
  }).then(async function(data){
        delete data.Data.Aggregated;
        delete data.Data.TimeFrom;
        delete data.Data.TimeTo;
        var minData = data.Data;
        var modify = JSON.stringify(minData);
        modify = modify.replace("Data", "3 hours");
        var jobj = JSON.parse(modify);
        await admin.firestore().collection("CoinData").doc(symbol).set(jobj, {merge: true});
  })
      //console.log("put in");
}

/**
 * 
 * @param {*} str String that you need to replace with
 * it should contains a string that replace with xxx
 * ex: BTC,ETH,BNB
 */
async function getCurrentCrtyData(str){
  var url_Data_multiple = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=XXX&tsyms=USD&50a7d1dd9da3909f5cdb9ff9f3c1a7c771a55ae0fdf57ef76988c10542de112a";
  var newurl_data = url_Data_multiple.replace('XXX', str);
  fetch(newurl_data).then(function(response){
    return response.json();
  }).then(async function(data){
        var rawData = data.RAW;
        for(var key in rawData){
          var newObj = {};
          newObj.name = await (await admin.firestore().collection('Top30').doc(rawData[key].USD.FROMSYMBOL).get()).data().name;
          newObj.MarketCap = rawData[key].USD.MKTCAP;
          newObj.Volume24 = rawData[key].USD.TOTALTOPTIERVOLUME24HTO;
          newObj.rateOfChange = rawData[key].USD.CHANGEPCTDAY;
          newObj.price = rawData[key].USD.PRICE;
          await admin.firestore().collection("CoinData").doc(rawData[key].USD.FROMSYMBOL).set(newObj, {merge: true});
        }
  })
      console.log("update list of coin");
}





/**
 * auto function calls every 5 minutes
 */

var auto = setInterval(async function() {
  await get_AllData_From_API();
  console.log("auto update");
}, 60000);






app.get("/try", async function(req, res){
  get_AllData_From_API();

  res.send("done");
})

app.post("/try2", async function (req, res){
  var str1 = req.body.username;
  var str2 = JSON.stringify(str1);
  res.send(str2);
})

app.post("/login", async function (req, res){
  var account = req.body.username;
  var password = req.body.password;
  console.log(account);
  var loginStatu = false;
  
  var users = await admin.firestore().collection("User").get();
  for(var i = 0 ; i< users.docs.length; i++){
    usedName =  users.docs[i].data().username;
    if(account === usedName){
      if(password === users.docs[i].data().password){
        loginStatu = true;
      }
    }
  }
  var statusJson = {};
  if(loginStatu == false){
    statusJson.status = "fail to login, incorrect account and password"
  }
  else{
    statusJson.status = "You have successful login"
  }
  res.send(statusJson);
})


app.post("/register", async function (req, res){
  var regName = req.body.username;
  var regPassWord = req.body.password;
  var repeatName = false;
  var users = await admin.firestore().collection("User").get();
  var usedName = "";
  for(var i = 0; i < users.docs.length; i++){
    usedName =  users.docs[i].data().username;

    if(regName === usedName){
      repeatName = true;
      break;
    }
  }
  if(!repeatName){
    var id = users.docs.length;
    var newUser = {};
    newUser.username = regName;
    newUser.password = regPassWord;
    newUser.id = users.docs.length;
    newUser.alertItem = [];
    newUser.savedItem = [];
    newUser.wishList = [];
    await admin.firestore().collection("User").doc("" + id).set(newUser, {merge: true});
  }
  
  var SuccessWords = "Registration is done";
  var failWords = "email is already taken";
  var returnWords = "";
  if(repeatName){
    returnWords = failWords;
  }else{
    returnWords = SuccessWords;
  }
  res.send(returnWords);
})

app.get("/getData", async function(req, res){
  var snapshot = await admin.firestore().collection("CoinData").get();
  
  var collection = [];
  for(var i = 0; i < snapshot.docs.length; i++)
  { 
      var data = snapshot.docs[i].data();
      collection.push(data);
  }
  
  console.log("200");
  res.send(collection);
})

app.get("/getTop5Rate", async function(req, res){
  var snapshot = await admin.firestore().collection("CoinData").orderBy("rateOfChange").get();
  var collection = [];
  var count = snapshot.docs.length - 1;
  for(var i = 0; i < 5; i++)
  {
    var data = snapshot.docs[count--].data();
    delete data.fifthteen_minutes;
      collection.push(data);
  }
  console.log("return top 5 rate");
  res.send(collection);
})

app.post("/getWishList", async function(req, res){
  var regName = req.body.username;
  var regPassWord = req.body.password;
  var login = false;
  var users = await admin.firestore().collection("User").get();
  var usedName = "";
  var usedPass = "";
  
  var returnData = {};
  for(var i = 0; i < users.docs.length; i++){
    usedName =  users.docs[i].data().username;
    usedPass =  users.docs[i].data().password;
    if(regName === usedName && regPassWord === usedPass){
      login = true;
      returnData.list = users.docs[i].data().wishList;
      break;
    }
  }

  if(login == false){
    returnData = "";
    
  }
  
  res.send(returnData);
})

/**
 * new, getting the names of top cryto from api
 */
app.get("/list", async function(req, res){
  fetch(newurl).then(function(response){
    return response.json();
  }).then(async function(data) {      

    for(var i = 0; i < data.Data.length; i++)
    {
        var name = data.Data[i].CoinInfo.FullName;
        var symbol = data.Data[i].CoinInfo.Internal;
        var obj = {};
        obj.name = name;
        obj.symbol = symbol;

        await admin.firestore().collection("Top30").doc(symbol).set(obj);
    }
    console.log("get list of 30 top coin");
    res.send(name);
  })
})




/**
 * teasting server works
 */
app.get("/",  function(req, res) {
  res.send("200");
  });

var port = process.env.PORT || 5000;

/**
 * run some start function here if you need
 */
app.listen(port, function() {
  console.log("app running");
})


//worked exmaples
//
//
//
//
/*
async function good1(){
  const docRef = db.collection('users').doc('alovelace');

  await docRef.set({
    first: 'Ada',
    last: 'Lovelace',
    born: 1815
  });
}

good1();
*/