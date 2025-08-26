#include <string>
#include <iostream>
#include <fstream>
std::string data = R"({
  "type": "iceandfire:dragonforge",
  "dragon_type": "DRAGON_TYPE",
  "cook_time": 2024,
  "input": {
    "item": "iceandfire:dragonarmor_MATERIAL_PART"
  },
  "blood": {
    "item": "minecraft:dragon_breath"
  },
  "result": {
    "item": "extended_tinker:dragon_armor_MATERIAL_core_PART",
    "nbt": {
      "Material": "extended_tinker:dragon_armor_CORE"
    }
  }
})",
			id = "dragon_armor_MATERIAL_core_PART_DRAGON_TYPE.json";
std::string replace(std::string s, std::string a, std::string b) noexcept {
	for (size_t i; (i = s.find(a)) != std::string::npos;) s.replace(i, a.size(), b);
	return s;
}
std::string generate(std::string data, std::string dragon_type, std::string material, std::string part, std::string core) noexcept {
	return replace(replace(replace(replace(data, "DRAGON_TYPE", dragon_type), "MATERIAL", material), "PART", part), "CORE", core);
}
signed main() {
	for (std::string part : {"head", "body", "neck", "tail"})
		for (std::string dragon_type : {"fire", "ice", "lightning"}) {
			for (std::string material : {"iron", "gold", "copper", "silver", "diamond"}) {
				std::string json = generate(data, dragon_type, material, part, material);
				std::ofstream of(generate(id, dragon_type, material, part, ""));
				of << json;
			}
			std::string json = generate(data, dragon_type, "dragonsteel_" + dragon_type, part, dragon_type);
			std::ofstream of(generate(id, dragon_type, "dragonsteel_" + dragon_type, part, ""));
			of << json;
		}
	return 0;
}