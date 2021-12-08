#import mysql.connector
import requests
import json
import datetime

"""mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  password="",
  database="summarization"
)

mycursor = mydb.cursor()

mycursor.execute("CREATE DATABASE summarization")"""

#sql = "DROP TABLE abstat"

#mycursor.execute(sql) 

#SELECT distinct property, count(property) FROM summarization.abstat group by property order by count(property) desc

#mycursor.execute("CREATE TABLE propriedades_distintas (property VARCHAR(255), active_10 VARCHAR(255), active_100 VARCHAR(255), active_535 VARCHAR(255))")


#mycursor.execute("CREATE TABLE abstat (property VARCHAR(255), object VARCHAR(255), frequency VARCHAR(255), avgS VARCHAR(255))")


"""distinct_property = mycursor.execute("SELECT distinct property FROM abstat")

i = 0
myresult = mycursor.fetchall()
for x in myresult:
  sql = "INSERT INTO propriedades_distintas (property) VALUES (%s)"
  mycursor.execute(sql, x)
  mydb.commit()
  i=i+1
  print(x, i)"""

#Id for 2016 DBpedia
summaryId = "986e1d8d-d4ab-4442-a569-6bc9987961ca"

result = requests.get("http://abstat.disco.unimib.it/api/v1/browse?summary=986e1d8d-d4ab-4442-a569-6bc9987961ca&subj=http://dbpedia.org/ontology/Film")
json_result = json.loads(result.text)

#print(result.text)

#print(json_result['akps'][0])
abstat_list = json_result['akps']
print(len(abstat_list))

"""for i in abstat_list:
    sql = "INSERT INTO abstat_mus_artist (property, object, frequency, avgS) VALUES (%s, %s, %s, %s)"
    val = (i.get('predicate'), i.get('object'), i.get('frequency'), i.get('cardinality5'))
    mycursor.execute(sql, val)
    mydb.commit()
    print(datetime.datetime.now())"""

"""mycursor.execute("SELECT * FROM abstat")


myresult = mycursor.fetchall()
for x in myresult:
  print(x) """

#print(mycursor.rowcount, "record inserted.")
