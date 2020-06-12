<template>
  <div class="q-pa-md">
    <div class="row">
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>
              <center>Produk</center>
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <q-carousel
                animated
                v-model="slide"
                navigation
                infinite
                autoplay
                arrows
                transition-prev="slide-right"
                transition-next="slide-left"
                style="max-height: 300px"
              >
                <q-carousel-slide :name="1"
                                  img-src="https://drive.google.com/uc?export=view&id=1Q2menqRKb3a0sLAGKqUQLWAQk7h0uDHi">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Pemasukan Barang</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="2"
                                  img-src="https://drive.google.com/uc?export=view&id=1VT-T7tggOOmwMxdc4hHoCS_MZIECeku7">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Barang Masuk</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="3"
                                  img-src="https://drive.google.com/uc?export=view&id=1Ec8F0TxCtzXhunfwngBSXaTOB74KCqE8">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Laporan Barang Masuk</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="4"
                                  img-src="https://drive.google.com/uc?export=view&id=1dLL2hObIHX3OVNvovMTLJeFZ5FGyl7wr">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Vendor Masuk</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="5"
                                  img-src="https://drive.google.com/uc?export=view&id=1J2n9cVL1Otn6fdo722WMcRSl0y9rlN6G">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Pengakasiran</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="6"
                                  img-src="https://drive.google.com/uc?export=view&id=1Vb3vzKap5ErlxAtAvePfX0xtglxccu3M">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Transaksi Produk</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="7"
                                  img-src="https://drive.google.com/uc?export=view&id=1IRTsDIMsAvhxShgEBeL-YBw8mtOLZM3_">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Produk Koperasi</div>
                  </div>
                </q-carousel-slide>
              </q-carousel>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>
              <center>Pinjaman</center>
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <q-carousel
                animated
                v-model="slide1"
                navigation
                infinite
                autoplay
                arrows
                transition-prev="slide-right"
                transition-next="slide-left"
                style="max-height: 300px"
              >
                <q-carousel-slide :name="1"
                                  img-src="https://drive.google.com/uc?export=view&id=1FYWNu0pp0Wmkw-5xVqFswPJwJCMOdXWy">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Pengajuan Pinjaman Baru</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="2"
                                  img-src="https://drive.google.com/uc?export=view&id=1-MlSFPqXj8NzQgkq5_4vL-EdtcEwyXqq">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Pinjaman Baru User</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="3"
                                  img-src="https://drive.google.com/uc?export=view&id=1Mn3_BJdJtIaBOD_WlY-hTDEdurxuStGG">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Pengajuan Pinjaman</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="4"
                                  img-src="https://drive.google.com/uc?export=view&id=1ST63AXvsth_ceer-wtwqxnX_oMHXPK1A">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Approve Pengajuan</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="5"
                                  img-src="https://drive.google.com/uc?export=view&id=1H-iZ1qcmVswbc-vIFQgg_lDqFoKe9OLe">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Tabel Angsuran</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="6"
                                  img-src="https://drive.google.com/uc?export=view&id=1JeMeEMZWdEgY6GK8vLb_0i4Yk9mbpYLD">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Daftar Pengajuan Telah Approve</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="7"
                                  img-src="https://drive.google.com/uc?export=view&id=1rnKjrsZ--DEqoRaCNzzjbM-ZJrbW0FfX">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Pembayaran Angsuran</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="8"
                                  img-src="https://drive.google.com/uc?export=view&id=1hYbYWUxxcIvk8tz63iVq7AEcyT4ocFHj">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Laporan Pinjaman</div>
                  </div>
                </q-carousel-slide>
              </q-carousel>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>
              <center>Simpanan</center>
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <q-carousel
                animated
                v-model="slide2"
                navigation
                infinite
                autoplay
                arrows
                transition-prev="slide-right"
                transition-next="slide-left"
                style="max-height: 300px"
              >
                <q-carousel-slide :name="1"
                                  img-src="https://drive.google.com/uc?export=view&id=13eOj6rePpnGxkDJDnMhRlDFCwdDYP__V">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Aktivasi Simpanan</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="2"
                                  img-src="https://drive.google.com/uc?export=view&id=1dmUGRjeCMyIysg5HMo0JTIgsp7h7vD_P">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Transaksi Simpanan</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="3"
                                  img-src="https://drive.google.com/uc?export=view&id=1_ugAvBc525YrnijY4D6qS0CeUS9NxqBR">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Setor Dana</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="4"
                                  img-src="https://drive.google.com/uc?export=view&id=1nuB4SaZBmuT1SIuQVd4Z_1-tvWIvRRJW">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Penarikan Dana</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="5"
                                  img-src="https://drive.google.com/uc?export=view&id=1N_dYl8Gyfx3SHX_Eb6dUTBPxic6SczEP">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">History Transaksi Simpanan(Anggota)</div>
                  </div>
                </q-carousel-slide>
                <q-carousel-slide :name="6"
                                  img-src="https://drive.google.com/uc?export=view&id=1q6C6deTZpwpxGUVcGMTxNRlsbh7xKE_s">
                  <div class="absolute-bottom custom-caption">
                    <div class="text-subtitle1">Saldo Anggota</div>
                  </div>
                </q-carousel-slide>
              </q-carousel>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        slide: 1,
        slide1: 1,
        slide2: 1
      }
    }
  }
</script>

<style scoped>

</style>
