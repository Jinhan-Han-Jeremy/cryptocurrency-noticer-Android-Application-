package com.example.crypto_noticer;

public class cryptoclass
{
    private String name;
    private String price;
    private String rates;
    private String marketCap;
    private String volume;


    public cryptoclass()
    {
        name = "" ;
        price = "" ;
        rates = "" ;
        marketCap = "" ;
        volume = "" ;
    }

    public cryptoclass(String initialname, String initialprice, String initialrates, String initialmarketCap, String initialvolume)
    {
        name = initialname;
        price = initialprice;
        rates = initialrates;
        marketCap = initialmarketCap;
        volume = initialvolume;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String givenname)
    {
        name = givenname;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String givenprice)
    {
        price = givenprice;
    }

    public String getMarketcap()
    {
        return marketCap;
    }

    public void setMarketcap(String givenmarket)
    {
        marketCap = givenmarket;
    }

    public String getVolume()
    {
        return volume;
    }

    public void setVolume(String givenvolume)
    {
        volume = givenvolume;
    }

    public String getRates()
    {
        return rates;
    }

    public void setRates(String givenrates)
    {
        rates = givenrates;
    }



}