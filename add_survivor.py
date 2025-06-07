import json

def add_survivor():
    lat = float(input("Enter survivor latitude: "))
    lon = float(input("Enter survivor longitude: "))
    new_location = {"lat": lat, "lon": lon}

    try:
        with open("survivors.json", "r") as f:
            data = json.load(f)
    except FileNotFoundError:
        data = []

    data.append(new_location)

    with open("survivors.json", "w") as f:
        json.dump(data, f)

    print("Survivor location added.")

add_survivor()
