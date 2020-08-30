package io.github.pokefoo.data.database.models.pokemon

import androidx.room.TypeConverter
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites
import org.json.JSONObject

public class PokemonTypeConverter
{
	@TypeConverter
	fun fromPokemonSprites(pokemonSprites: PokemonSprites?): String?
	{
		val json = JSONObject()
		json.put("BACK_DEFAULT", pokemonSprites?.backDefault ?: "")
		json.put("BACK_SHINY", pokemonSprites?.backShiny ?: "")
		json.put("FRONT_DEFAULT", pokemonSprites?.frontDefault ?: "")
		json.put("FRONT_SHINY", pokemonSprites?.frontShiny ?: "")
		json.put("BACK_FEMALE", pokemonSprites?.backFemale ?: "")
		json.put("BACK_SHINY_FEMALE", pokemonSprites?.backShinyFemale ?: "")
		json.put("FRONT_FEMALE", pokemonSprites?.frontFemale ?: "")
		json.put("FRONT_SHINY_FEMALE", pokemonSprites?.frontShinyFemale ?: "")
		return json.toString()
	}

	@TypeConverter
	fun fromString(pokemonSpritesJson: String?): PokemonSprites?
	{
		return when
		{
			!pokemonSpritesJson.isNullOrBlank() ->
			{
				JSONObject(pokemonSpritesJson).let { json ->
					PokemonSprites(
                        json.getString("BACK_DEFAULT"),
                        json.getString("BACK_SHINY"),
                        json.getString("FRONT_DEFAULT"),
                        json.getString("FRONT_SHINY"),
                        json.getString("BACK_FEMALE"),
                        json.getString("BACK_SHINY_FEMALE"),
                        json.getString("FRONT_FEMALE"),
                        json.getString("FRONT_SHINY_FEMALE")
                    )
				}
			}
			else -> PokemonSprites(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
		}
	}
}