import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv('cc.csv', sep=';', index_col =0)


print("Aperçu des données :")
print(df.head())


average_cc_per_class = df.groupby('Class')['CC'].mean()

# Création de l'histogramme
plt.figure(figsize=(10, 6))
average_cc_per_class.plot(kind='bar', color='skyblue')
plt.xlabel('Classe')
plt.ylabel('Complexité cyclomatique ')
plt.title('Histogramme des Complexités Cyclomatiques moyenne par classe')
plt.grid(axis='x', linestyle='--', alpha=0.7)

# Afficher l'histogramme
plt.show()