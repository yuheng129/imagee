import tkinter as tk
from tkinter import messagebox
import json
import folium
import webbrowser
from folium import MacroElement
from jinja2 import Template

# File paths
SURVIVOR_FILE = "survivors.json"
MAP_FILE = "interface_map.html"

# Simulated robot location
ROBOT_LAT = 3.1390
ROBOT_LON = 101.6869
ZOOM_START = 16

def save_survivor(lat, lon):
    try:
        with open(SURVIVOR_FILE, "r") as f:
            data = json.load(f)
    except FileNotFoundError:
        data = []

    data.append({"lat": lat, "lon": lon})

    with open(SURVIVOR_FILE, "w") as f:
        json.dump(data, f)

class CenterButton(MacroElement):
    _template = Template("""
        {% macro script(this, kwargs) %}
        var customControl = L.Control.extend({
            options: { position: 'topright' },
            onAdd: function (map) {
                var container = L.DomUtil.create('button', 'leaflet-bar leaflet-control leaflet-control-custom');
                container.style.backgroundColor = 'white';
                container.style.width = '120px';
                container.style.height = '30px';
                container.style.cursor = 'pointer';
                container.style.fontSize = '14px';
                container.innerHTML = 'Center on Robot';

                container.onclick = function(){
                    map.setView([{{this.lat}}, {{this.lon}}], {{this.zoom}});
                };
                return container;
            }
        });
        map.addControl(new customControl());
        {% endmacro %}
    """)

    def __init__(self, lat, lon, zoom):
        super().__init__()
        self.lat = lat
        self.lon = lon
        self.zoom = zoom

def generate_map():
    try:
        with open(SURVIVOR_FILE, "r") as f:
            survivors = json.load(f)
    except FileNotFoundError:
        survivors = []

    m = folium.Map(location=[ROBOT_LAT, ROBOT_LON], zoom_start=ZOOM_START)
    folium.Marker([ROBOT_LAT, ROBOT_LON], popup="Robot", icon=folium.Icon(color="blue")).add_to(m)

    for s in survivors:
        folium.Marker([s["lat"], s["lon"]], popup="Survivor", icon=folium.Icon(color="red")).add_to(m)

    # Add the Center on Robot button inside the map
    m.add_child(CenterButton(ROBOT_LAT, ROBOT_LON, ZOOM_START))

    m.save(MAP_FILE)

def on_submit():
    try:
        lat = float(entry_lat.get())
        lon = float(entry_lon.get())
        save_survivor(lat, lon)
        generate_map()
        messagebox.showinfo("Success", "Survivor added and map updated!")
    except ValueError:
        messagebox.showerror("Error", "Please enter valid numbers.")

root = tk.Tk()
root.title("Survivor Location Adder")

tk.Label(root, text="Latitude:").grid(row=0, column=0, padx=5, pady=5)
entry_lat = tk.Entry(root)
entry_lat.grid(row=0, column=1, padx=5, pady=5)

tk.Label(root, text="Longitude:").grid(row=1, column=0, padx=5, pady=5)
entry_lon = tk.Entry(root)
entry_lon.grid(row=1, column=1, padx=5, pady=5)

submit_btn = tk.Button(root, text="Add Survivor & Update Map", command=on_submit)
submit_btn.grid(row=2, column=0, columnspan=2, pady=10)

root.mainloop()