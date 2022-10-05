/**
 * Copyright (C) 2006-2021 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export const QualifierTypeData = [
    {option: "Specimen Voucher (e.g. specimen)", value: "specimen_voucher"},
    {option: "Culture Collection (e.g. live microbial, viral cultures, cell lines)", value: "culture_collection"},
    {option: "Bio Material (e.g. tissues, DNA, seed banks, zoos, germplasms)", value: "bio_material"},
];

export const QualifierTypeDisplay = new Map()
    .set("specimen_voucher","Specimen Voucher")
    .set("culture_collection","Culture Collection")
    .set("bio_material","Bio Material");